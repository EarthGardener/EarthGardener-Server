package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.Tree;
import com.stopclimatechange.earthgarden.domain.TreeDto;
import com.stopclimatechange.earthgarden.domain.User;
import com.stopclimatechange.earthgarden.repository.TreeRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TreeService {

    private final TreeRepository treeRepository;

    public void changeTreeName(User user, TreeDto.NameDto nameDto){
        Tree tree = user.getTree();
        TreeDto treeDto = new TreeDto(tree);
        treeDto.setName(nameDto.getName());
        tree.updateByTreeDto(treeDto);
        treeRepository.save(tree);
    }

    public TreeDto returnTreeInfo(User user){
        Tree tree = user.getTree();
        TreeDto treeInfo = new TreeDto(tree);
        return treeInfo;
    }

    public Boolean isTreeLevelUp(){
        return false;
    }
}
