package org.ifpe.recicoin.service;

import org.ifpe.recicoin.entities.CollectionPoint;
import org.ifpe.recicoin.repositories.CollectionPointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CollectionPointService {
    @Autowired
    private CollectionPointRepository collectionPointRepository;

    public CollectionPoint createCollectionPoint(CollectionPoint collectionPoint) {
        return collectionPointRepository.save(collectionPoint);
    }

    public CollectionPoint updateCollectionPoint(Long id, CollectionPoint newCollectionPoint) {
        CollectionPoint oldCollectionPoint = collectionPointRepository.findById(id).get();

        oldCollectionPoint.setName(newCollectionPoint.getName());
        oldCollectionPoint.setPhone(newCollectionPoint.getPhone());
        oldCollectionPoint.setAddress(newCollectionPoint.getAddress());
        oldCollectionPoint.setCity(newCollectionPoint.getCity());
        oldCollectionPoint.setState(newCollectionPoint.getState());
        oldCollectionPoint.setZipcode(newCollectionPoint.getZipcode());
        oldCollectionPoint.setOpeningTime(newCollectionPoint.getOpeningTime());
        oldCollectionPoint.setClosingTime(newCollectionPoint.getClosingTime());
        oldCollectionPoint.setDescription(newCollectionPoint.getDescription());

        return collectionPointRepository.save(oldCollectionPoint);
    }

    public void deleteCollectionPoint(Long id) {
        collectionPointRepository.deleteById(id);
    }

    public Optional<CollectionPoint> findById(Long id) {
        return collectionPointRepository.findById(id);
    }

    public UserDetails findByEmail(String email) {
        return collectionPointRepository.findByEmail(email);
    }

    public List<CollectionPoint> findAll() {
        return collectionPointRepository.findAll();
    }
}
