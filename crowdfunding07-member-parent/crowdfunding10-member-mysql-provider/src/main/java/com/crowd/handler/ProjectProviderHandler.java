package com.crowd.handler;

import com.crowd.entity.vo.DetailProjectVO;
import com.crowd.entity.vo.PortalTypeVO;
import com.crowd.entity.vo.ProjectVO;
import com.crowd.service.api.ProjectService;
import com.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProjectProviderHandler {

    @Autowired
    private ProjectService projectService;

    @RequestMapping("/save/project/vo/remote")
    public ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId){

        try{
            projectService.saveProject(projectVO,memberId);
            return ResultEntity.successWithoutData();
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    };


    @RequestMapping("/get/portal/type/project/data/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote(){
        try{
            List<PortalTypeVO> portalTypeVO = projectService.getPortalTypeVO();

            return ResultEntity.successWithData(portalTypeVO);
        }catch (Exception e){
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    };


    @RequestMapping("/get/project/detail/remote/{projectId}")
    public ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId") Integer projectId) {

        try {
            DetailProjectVO detailProjectVO = projectService.getDetailProjectVO(projectId);

            return ResultEntity.successWithData(detailProjectVO);

        } catch (Exception e) {
            e.printStackTrace();

            return ResultEntity.failed(e.getMessage());
        }

    }
}
