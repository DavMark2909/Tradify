package com.tradify.application.service;

import com.tradify.application.dto.SavedItemDto;
import com.tradify.application.entity.Product;
import com.tradify.application.entity.SavedItem;
import com.tradify.application.entity.User;
import com.tradify.application.exception.ObjectNotFoundException;
import com.tradify.application.kafka.dto.ActivityEvent;
import com.tradify.application.kafka.dto.ActivityPayload;
import com.tradify.application.repository.ProductRepository;
import com.tradify.application.repository.SavedItemRepository;
import com.tradify.application.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SavedItemService {

    private final SavedItemRepository savedItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Transactional(readOnly = true)
    public Page<SavedItemDto> getSavedItems(Long userId, int pageNumber, int pageSize) {
        Pageable pageReq = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<SavedItem> items = savedItemRepository.findByBuyerId(userId, pageReq);
        Page<SavedItemDto> dtoPage = items.map(this::convertToDto);

        return dtoPage;
    }

    @Transactional
    public void createNewSavedItem(Long userId, long productId) {
//        using the power of hibernates to set the foreign key by referencing to the proxy object w/o the actual request to DB
//        HOWEVER: this approach is no longer valid as i need the actual instances to create a kafka event
//        savedItem.setBuyer(userRepository.getReferenceById(userId));
//        savedItem.setProduct(productRepository.getReferenceById(productId));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ObjectNotFoundException("Product not found"));
        User buyer = userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("User not found"));

        SavedItem savedItem = new SavedItem();
        savedItem.setBuyer(buyer);
        savedItem.setProduct(product);
        savedItemRepository.save(savedItem);

        Long companyId = buyer.getCompanyProfile() != null ? buyer.getCompanyProfile().getId() : null;
        ActivityPayload payload = new ActivityPayload(
                product.getId(),
                product.getSector().getId(),
                product.getSupplier().getId()
        );
        //        notifies listeners to call for a producer based on the type. Here - ActivityEvent
        eventPublisher.publishEvent(ActivityEvent.productSaved(userId, companyId, payload));
    }

    @Transactional
    public void removeSavedItem(Long itemId, Long userId) {
        long deletedCount = savedItemRepository.deleteByIdAndBuyer_Id(itemId, userId);

        if (deletedCount == 0) {
            throw new RuntimeException("Item not found or you do not have permission to delete it.");
        }
    }

    private SavedItemDto convertToDto(SavedItem item) {
        return new SavedItemDto(
                item.getId(),
                item.getProduct().getTitle(),
                item.getProduct().getDescription(),
                item.getProduct().getPrice(),
                item.getProduct().getCurrency(),
                item.getProduct().getUnitOfMeasure(),
                item.getProduct().getAvailableQuantity(),
                item.getProduct().getStatus(),
                item.getProduct().getSupplier().getName(),
                item.getCreatedAt()
        );
    }
}
