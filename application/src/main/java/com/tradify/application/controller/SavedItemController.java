package com.tradify.application.controller;

import com.tradify.application.dto.SavedItemDto;
import com.tradify.application.entity.User;
import com.tradify.application.service.SavedItemService;
import com.tradify.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/saved-items")
@RequiredArgsConstructor
public class SavedItemController {
    private final SavedItemService savedItemService;

    @GetMapping("/view")
    public ResponseEntity<Page<SavedItemDto>> view(JwtAuthenticationToken authenticationToken,
                                                   @RequestParam(defaultValue = "20") int page,
                                                   @RequestParam(defaultValue = "0") int size
    ){
        Long userId = (Long) authenticationToken.getTokenAttributes().get("userId");
        return ResponseEntity.ok(savedItemService.getSavedItems(userId, page,size));
    }

    @PostMapping("/create")
    public ResponseEntity<Void> saveNewItem(JwtAuthenticationToken authenticationToken,
                                      @RequestParam long id
    ){
        // id is a reference to the underlying product
        Long userId = (Long) authenticationToken.getTokenAttributes().get("userId");
        savedItemService.createNewSavedItem(userId, id);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteSavedItem(JwtAuthenticationToken authenticationToken,
                                                @RequestParam long id
    ){
        Long userId = (Long) authenticationToken.getTokenAttributes().get("userId");
        savedItemService.removeSavedItem(userId, id);

        return ResponseEntity.noContent().build();
    }

}
