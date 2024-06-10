package com.nea.backend.repository;

import com.nea.backend.model.Request;
import com.nea.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    Request findByUser(User user);
    List<Request> findAllByApprovedFalse();
}
