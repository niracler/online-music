package com.groupten.online_music.web;

import com.groupten.online_music.common.utils.ApplicationException;
import com.groupten.online_music.common.utils.ResponseEntity;
import com.groupten.online_music.common.utils.STablePageRequest;
import com.groupten.online_music.entity.User;
import com.groupten.online_music.service.impl.IUserManageService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Api(tags = "用户管理相关接口")
@RestController
@RequestMapping("/admin")
public class UserManageController {
    @Autowired
    private IUserManageService userManageService;

    @ApiOperation(value = "新增用户接口")
    @PostMapping
    public ResponseEntity<User> add(@RequestParam Map<String, String> userMap){
        User user = new User(userMap);
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        if(!userManageService.hasUser(user) && userManageService.findByEmail(user.getEmail())==null){
            user = userManageService.save(user);
        } else {
            return responseEntity.status(HttpStatus.BAD_REQUEST).message("有重名用户或邮箱已注册").data(null);
        }

        return responseEntity.status(HttpStatus.CREATED).message("用户添加成功").data(user);
    }

    @ApiOperation(value = "用户分页查询接口")
    @GetMapping
    public @ResponseBody ResponseEntity<Page<User>> FindAll(@RequestParam Map<String, String> pagingMap){
        ResponseEntity<Page<User>> responseEntity = new ResponseEntity<>();
        STablePageRequest tablePageRequest = new STablePageRequest(pagingMap);
        Page<User> page;
        try {
            page = Page.empty(tablePageRequest.sTablePageable());
            page = userManageService.findAll( tablePageRequest.sTablePageable());
        } catch (ApplicationException ex) {
            return responseEntity.success(false).status(HttpStatus.BAD_REQUEST).message("分页查询失败");
        }

        return responseEntity.success(true).status(HttpStatus.OK).message("分页查询成功").data(page);
    }

    @ApiOperation("删除用户接口")
    @DeleteMapping("/{id}")
    public @ResponseBody
    ResponseEntity delete(@PathVariable int id) {
        //响应结果
        ResponseEntity<User> responseEntity = new ResponseEntity<User>();
        boolean result = userManageService.deleteById(id);
        return responseEntity.success(result)
                .status(result ? HttpStatus.NO_CONTENT : HttpStatus.BAD_REQUEST)
                .message(result ? "删除请求成功" : "删除请求失败");
    }
}
