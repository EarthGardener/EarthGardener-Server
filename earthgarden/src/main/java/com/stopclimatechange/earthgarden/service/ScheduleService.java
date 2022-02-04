package com.stopclimatechange.earthgarden.service;

import com.stopclimatechange.earthgarden.domain.Tree;
import com.stopclimatechange.earthgarden.domain.TreeDto;
import com.stopclimatechange.earthgarden.repository.TreeRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ScheduleService {

    private final TreeRepository treeRepository;

    @Scheduled(cron = "1 0 0 1 * ?")
    private void initializeTreeCountEveryMonth(){
        List<Tree> trees = treeRepository.findAll();
        for(Tree tree : trees){
            TreeDto treeDto = new TreeDto(tree);
            treeDto.setMonth_sum(0);
            tree.updateByTreeDto(treeDto);
            treeRepository.save(tree);
        }
    }
}
