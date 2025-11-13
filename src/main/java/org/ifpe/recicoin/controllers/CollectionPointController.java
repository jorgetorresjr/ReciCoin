package org.ifpe.recicoin.controllers;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.ifpe.recicoin.repositories.CollectionPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/collection-point")
public class CollectionPointController {

    @Autowired
    private CollectionPointRepository collectionPointRepository;

    @GetMapping
    public ResponseEntity<CollectionPoint> getLoggedCollectionPoint() {
        CollectionPoint collectionPoint = (CollectionPoint) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(collectionPoint);
    }


    @PutMapping
    public ResponseEntity<?> updateLoggedCollectionPoint(@RequestBody CollectionPoint newCollectionPoint) {
        CollectionPoint collectionPoint = (CollectionPoint) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();

        collectionPoint.setName(newCollectionPoint.getName());
        collectionPoint.setPhone(newCollectionPoint.getPhone());
        collectionPoint.setAddress(newCollectionPoint.getAddress());
        collectionPoint.setCity(newCollectionPoint.getCity());
        collectionPoint.setState(newCollectionPoint.getState());
        collectionPoint.setZipcode(newCollectionPoint.getZipcode());
        collectionPoint.setDescription(newCollectionPoint.getDescription());
        collectionPoint.setOpeningTime(newCollectionPoint.getOpeningTime());
        collectionPoint.setClosingTime(newCollectionPoint.getClosingTime());

        collectionPointRepository.save(collectionPoint);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLoggedCollectionPoint() {
        CollectionPoint collectionPoint = (CollectionPoint) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        collectionPointRepository.delete(collectionPoint);

        return ResponseEntity.ok().build();
    }
}
