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

    public Boolean isTreeLevelUp(TreeDto treeDto){

        if (treeDto.getExp() >= (treeDto.getLevel()+1)*500){
            return true;
        }
        return false;
    }

    public TreeDto reflectLevel(User user){
        Tree tree = user.getTree();
        TreeDto treeDto = new TreeDto(tree);
        if(tree.getLevel() + 1 == 6){
            treeDto.setMonth_sum(treeDto.getMonth_sum()+1);
            treeDto.setTotal_sum(treeDto.getTotal_sum()+1);
            treeDto.setLevel(1);
        }
        else {
            treeDto.setLevel(tree.getLevel() + 1);
        }
        treeDto.setExp(tree.getExp()- (tree.getLevel()+1)*500);
        tree.updateByTreeDto(treeDto);
        treeRepository.save(tree);
        return treeDto;
    }

    public void updateExp(User user, Integer exp){
        Tree tree = user.getTree();
        TreeDto treeDto = new TreeDto(tree);
        treeDto.setExp(tree.getExp() + exp);
        tree.updateByTreeDto(treeDto);
        treeRepository.save(tree);
    }
}
