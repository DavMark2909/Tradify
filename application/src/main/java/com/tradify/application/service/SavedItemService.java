package com.tradify.application.service;

import com.tradify.application.dto.SavedItemDto;
import com.tradify.application.entity.SavedItem;
import com.tradify.application.entity.User;
import com.tradify.application.repository.ProductRepository;
import com.tradify.application.repository.SavedItemRepository;
import com.tradify.application.repository.UserRepository;
import lombok.AllArgsConstructor;
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


    @Transactional(readOnly = true)
    public Page<SavedItemDto> getSavedItems(Long userId, int pageNumber, int pageSize) {
        Pageable pageReq = PageRequest.of(pageNumber, pageSize, Sort.by("createdAt").descending());
        Page<SavedItem> items = savedItemRepository.findByBuyerId(userId, pageReq);
        Page<SavedItemDto> dtoPage = items.map(this::convertToDto);

        return dtoPage;
    }

    @Transactional
    public void createNewSavedItem(Long userId, long productId) {
        SavedItem savedItem = new SavedItem();
//        using the power of hibernates to set the foreign key by referencing to the proxy object w/o the actual request to DB
        savedItem.setBuyer(userRepository.getReferenceById(userId));
        savedItem.setProduct(productRepository.getReferenceById(productId));
        savedItemRepository.save(savedItem);
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
