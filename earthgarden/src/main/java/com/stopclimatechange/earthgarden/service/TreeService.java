package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.Tree;
import com.stopclimatechange.earthgarden.domain.TreeDto;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.repository.TreeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TreeService {

    private final TreeRepository treeRepository;

    public void changeTreeName(User user, TreeDto.NameDto nameDto){
        Tree tree = user.getTree();
        tree.updateTreeName(nameDto.getName());
        treeRepository.save(tree);
    }
}
