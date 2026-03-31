package com.heima.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.AuthDto;
import com.heima.model.user.dtos.UserRelationDto;
import com.heima.model.user.pojos.ApUserRealname;
import org.hamcrest.core.Is;

import javax.xml.ws.Response;

public interface ApUserRelnameService extends IService<ApUserRealname> {
    /**
     * 按照状态分页查询用户列表
     * @param dto
     * @return
     */
    public ResponseResult loadListByStatus(AuthDto dto);

    /**
     * @param dto
     * @param Status 2 审核失败  9审核成功
     * @return
     */
    public ResponseResult updateStatus(AuthDto dto, Short Status);
}
