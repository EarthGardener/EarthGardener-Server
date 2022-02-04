package com.stopclimatechange.earthgarden.repository;

import com.stopclimatechange.earthgarden.domain.Tree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TreeRepository extends JpaRepository<Tree, Long> {
    public Tree findById(String id);
}
