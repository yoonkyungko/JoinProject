package com.yoon.joinproject.controller;

import com.yoon.joinproject.service.UserService;
import com.yoon.joinproject.vo.UserVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class UserController {
    @Autowired
    UserService userService;

    //회원가입
    @GetMapping("/Join")
    public String joinForm(){
        return "Join";
    }

    //회원가입 진행
    @PostMapping("/JoinDone")
    @ResponseBody
    public Map<String, Object> join(UserVo userVo){
        Map<String, Object > rtnMap = new HashMap<>();
        try{
            String userId = userService.searchUser(userVo);
            if(!(userId == null || userId.equals(""))){
                rtnMap.put("procMsg", "이미 존재하는 회원ID입니다.");
                return rtnMap;
            }

            int procCnt = procCnt = userService.joinUser(userVo);
            if(procCnt > 0) {
                rtnMap.put("procYn", "Y");
                rtnMap.put("procMsg", "회원가입에 성공했습니다.");
            }

        }catch (Exception e){
            rtnMap.put("errMsg", "join fail");
        }

        return rtnMap;
    }

    // 인증번호 생성
    @GetMapping("/AuthCreate")
    @ResponseBody
    public Map<String, Object> authCreate(HttpServletResponse response, HttpSession session){
        Map<String, Object > rtnMap = new HashMap<>();
        String authNum = "";

        authNum = authNumCreate();

        if(authNum.length() == 6){
            rtnMap.put("crYn", "Y");
            rtnMap.put("authNum", authNum);

            session.setAttribute("authNum", authNum);
        }else{
            rtnMap.put("crYn", "N");
        }

        return rtnMap;
    }

    // 인증번호 생성
    private String authNumCreate(){
        Random rand  = new Random();
        String authNum = "";
        for(int i=0; i<6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            authNum+=ran;
        }
        return authNum;
    }

    // 인증번호 확인
    @PostMapping("/AuthCheck")
    @ResponseBody
    public Map<String, Object > authCheck(HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Map<String, Object > rtnMap = new HashMap<>();
        String authNum = session.getAttribute("authNum").toString();
        String userInpNum = request.getParameter("userInpNum");

        if(authNum.equals(userInpNum)){
            rtnMap.put("chkYn", "Y");

        } else {
            rtnMap.put("chkYn", "N");
        }
        return rtnMap;
    }

    //로그인
    @GetMapping("/Login")
    public String loginView(){
        return "Login";
    }

    @PostMapping("/LoginDone")
    @ResponseBody
    public Map<String, Object> login(UserVo userVo, HttpSession session){
        Map<String, Object > rtnMap = new HashMap<>();
        Map<String, Object > userInfo = new HashMap<>();
        Map<String, Object > pwdInfo = new HashMap<>();
        try{
            userInfo = userService.loginCheck(userVo);
            pwdInfo  = userService.pwdCheck(userVo);

            if(userInfo != null){
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String tempPwd = userVo.getUserPwd();

                if(!encoder.matches(tempPwd, (String) pwdInfo.get("USERPWD"))){
                    rtnMap.put("loginMsg", "비밀번호가 일치하지 않습니다.");
                    return rtnMap;
                }

                rtnMap.put("loginYn", "Y");
                rtnMap.put("loginMsg", "로그인에 성공했습니다.");

                session.setAttribute("userInfo", userInfo);
            }else {
                rtnMap.put("loginMsg", "입력한 정보와 일치하는 사용자가 없습니다.");
                return rtnMap;
            }

        }catch (Exception e){
            rtnMap.put("errMsg", "login fail");
        }

        return rtnMap;
    }

    @GetMapping("/LoginComplete")
    public String loginComplete(HttpSession session, Model model){
        Map<String, Object > userInfo = new HashMap<>();
        userInfo = (Map<String, Object>) session.getAttribute("userInfo");

        model.addAttribute("userInfo", userInfo);

        return "LoginComplete";
    }

    // 비밀번호 재설정 처리
    @PostMapping("/PwdChg")
    @ResponseBody
    public Map<String, Object> pwdChg(UserVo userVo, HttpServletRequest request){
        Map<String, Object > rtnMap = new HashMap<>();
        Map<String, Object > pwdInfo = new HashMap<>();
        try{
            userVo.setUserId(request.getParameter("chgUserId"));
            pwdInfo  = userService.pwdCheck(userVo);

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String tempPwd = request.getParameter("oriPwd");

            if(!encoder.matches(tempPwd, (String) pwdInfo.get("USERPWD"))){
                rtnMap.put("chgMsg", "기존 비밀번호가 맞지 않습니다.");
                return rtnMap;
            }

            userVo.setUserPwd(request.getParameter("chgPwd"));

            int updCnt = userService.updPwd(userVo);

            if(updCnt > 0){
                rtnMap.put("chgYn", "Y");
                rtnMap.put("chgMsg", "비밀번호 변경에 성공했습니다.");
            }

        }catch (Exception e){
            rtnMap.put("errMsg", "chg fail");
        }

        return rtnMap;
    }

    @PostMapping("/UserFind")
    @ResponseBody
    public Map<String, Object> userFind(UserVo userVo){
        Map<String, Object > rtnMap = new HashMap<>();
        try{
            String userId = userService.searchUser(userVo);
            System.out.println("user: "+userId);
            if(userId == null || userId.equals("")){
                rtnMap.put("findMsg", "존재하지 않는 회원입니다.");
                return rtnMap;
            }
            rtnMap.put("findYn", "Y");
            rtnMap.put("findMsg", "비밀번호 찾기(재설정)이 가능합니다.");

        }catch (Exception e){
            rtnMap.put("errMsg", "비밀번호 찾기(재설정)에 실패했습니다. 관리자에게 문의하세요.");
        }

        return rtnMap;
    }
}
