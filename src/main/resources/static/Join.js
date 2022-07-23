$(document).ready(function(){
    $("#authText").hide();
    $("#authCheck").hide();
    $("#tempNum").hide();

    $("#authStart").click(function (){ //인증번호 생성
        fn_authCreate();
    })

    $("#authCheck").click(function (){ //인증번호 확인
        fn_authCheck();
    })

    $("#joinComplete").click(function (){ //회원가입 유효성체크
        if(fn_userValidate()){
            fn_joinProcess();
        }

    })

});

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
                $("#authStart").val("다시 인증하기");

                $("#tempNum").text("* 테스트용 인증번호는 "+data.authNum + "입니다.");
                $("#tempNum").show();

                $("#authCheck").attr("disabled", false);
            } else{
                alert("인증번호 생성에 실패하였습니다. 관리자에게 문의해주세요.");
            }
        },
        error:function (data){
            alert("인증번호 생성에 실패하였습니다. 관리자에게 문의해주세요.");
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
            } else{
                alert("인증에 실패하였습니다. 다시 시도해주세요.");
            }

        },
        error:function (data){
            alert("인증에 실패하였습니다. 다시 시도해주세요.");
        }
    })
}

function fn_userValidate(){
    if($('#userId').val() == ''){
        alert('아이디를 입력하세요');
        return false;
    }else if($('#userPwd').val() == ''){
        alert('비밀번호를 입력하세요');
        return false;
    }else if($('#userName').val() == ''){
        alert('이름을를 입력하세요');
        return false;
    }else if($('#userPhone').val() == ''){
        alert('전화번호를 입력하세요');
        return false;
    }else if($("#authYn").val() != 'Y'){
        alert('전화번호 인증을 완료해주세요.');
        return false;
    }else{
        return true;
    }
}

// 회원가입
function fn_joinProcess(){
    var joinForm = $('#joinForm').serialize();
    $.ajax({
        url:'/JoinDone',
        type: "POST",
        data: joinForm,
        success:function(data){
            var procYn = data.procYn;
            if(procYn == "Y") {
                alert("회원가입에 성공했습니다. 로그인을 해주세요.");
                location.replace("/Login");
            } else{
                alert(data.procMsg);
            }

        },
        error:function (data){
            alert("회원가입 처리중 오류가 발생했습니다. 관리자에게 문의해주세요.");
        }
    })
}


