$(document).ready(function(){
    $("#loginComplete").click(function (){ //로그인 처리
        fn_loginProcess();
    })

    $("#pwdChange").click(function (){
        if($('#userId').val() == '') {
            alert('아이디를 입력하세요');
            return false;
        } else{
            fn_userFind();
        }
    })

    $("#chgStart").click(function (){ //비밀번호 재설정
        fn_changePwd();
    })

    $("#authCheck").click(function (){ //인증번호 확인
        fn_authCheck();
    })

    $("#authStart").click(function (){ //인증번호 생성
        fn_authCreate();
    })

});

function fn_loginProcess(){
    var useInputInfo = $('#useInputInfo').serialize();
    $.ajax({
        url:"/LoginDone",
        type: "POST",
        data: useInputInfo,
        success:function(data){
            var loginYn = data.loginYn;
            if(loginYn == "Y") {
                alert("로그인에 성공했습니다.");
                location.replace("/LoginComplete");
            } else{
                alert(data.loginMsg);
            }
        },
        error:function (data){
            alert("로그인 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    })
}

function fn_authCreate(){
    $("#authYn").val("N");
    $.ajax({
        url:'/AuthCreate',
        success:function(data){
            var crYn = data.crYn;
            if(crYn == "Y"){
                alert("인증번호 생성에 성공하였습니다. 인증확인 해주세요.");
                $("#authText").show();
                $("#authCheck").show();

                $("#tempNum").text("* 테스트용 인증번호는 "+data.authNum + "입니다.");
                $("#tempNum").show();

                $("#authCheck").attr("disabled", false);
                $("#chgPwdForm").hide();

            } else{
                alert("인증번호 생성에 실패하였습니다. 관리자에게 문의해주세요.");
            }
        },
        error:function (data){
            alert("인증번호 생성에 실패하였습니다. 관리자에게 문의해주세요.");
        }
    })
}

function fn_changePwd(){
    var chgPwd = $('#chgPwd').val();
    var chgPwd2 = $('#chgPwd2').val();
    var oriPwd = $('#oriPwd').val();

    if(oriPwd == ""){
        alert("기존 비밀번호가 입력되지 않았습니다.");
        return false;
    }

    if(chgPwd != chgPwd2){
        alert("재설정 비밀번호가 일치하지 않습니다.");
        return false;
    }

    $('#chgUserId').val($('#userId').val());

    var chgPwdForm = $('#chgPwdForm').serialize();
    $.ajax({
        url:'/PwdChg',
        type: "POST",
        data:chgPwdForm,
        success:function(data){
            var chgYn = data.chgYn;
            if(chgYn == "Y") {
                alert("비밀번호 변경에 성공했습니다.");
                location.reload();
            } else{
                alert(data.chgMsg);
            }

        },
        error:function (data){
            alert("비밀번호 변경에 실패하였습니다. 관리자에게 문의해주세요.");
        }
    })
}

function fn_authCheck(){
    var userInpNum = $("#authText").val();
    $.ajax({
        url:'/AuthCheck',
        type: "POST",
        data: {"userInpNum":userInpNum},
        success:function(data){
            var chkYn = data.chkYn;
            if(chkYn == "Y") {
                alert("인증에 성공했습니다.");
                $("#authCheck").attr("disabled", "disabled");
                $("#authYn").val("Y");

                $("#chgPwdForm").show();
            } else{
                alert("인증에 실패하였습니다. 다시 시도해주세요.");
            }

        },
        error:function (data){
            alert("인증에 실패하였습니다. 다시 시도해주세요.");
        }
    })
}

function fn_userFind(){
    var useInputInfo = $('#useInputInfo').serialize();
    $.ajax({
        url:'/UserFind',
        type: "POST",
        data: useInputInfo,
        success:function(data){
            var findYn = data.findYn;
            if(findYn == "Y") {
                alert(data.findMsg);
                $('#chgForm').show();
                $('#chgPwdForm').hide();
                fn_authCreate();
            } else{
                alert(data.findMsg);
            }

        },
        error:function (data){
            alert("회원가입 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    })
}