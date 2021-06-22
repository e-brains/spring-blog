let index={
    init:function(){
        $("#btn-save").on("click", ()=>{   // function(){}을 사용하지 않은 이유는 this를 바인딩하기 위해서 !!
            this.save();  //  function(){}을 사용하면 this가 윈도우 객체를 가리키게 되기 때문에 ()=>{}을 사용함
        });

        $("#btn-login").on("click", ()=>{  // loginForm.mustache에서 로그인 버튼이 클릭되면 ..
            this.login();
        })
    },

    /********
    회원가입
    **********/
    save:function(){
        //alert('user의 save()함수 호출됨');
        let data={
            username:$("#username").val(),  // ui의 username태그의 value를 username변수에 바인딩
            password:$("#password").val(),
            email:$("#email").val()
        };

        //console.log(data);

        // ajax 호출 시 default가 비동기 호출
        // ajax통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
        $.ajax({
            type:"POST",
            url:"/api/user/join",
            data:JSON.stringify(data),  // http body데이터, 위에서 정의한 자바스크립트 오브젝트인 data를 Json 문자열로 변환
            contentType:"application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
            dataType:"json" // 서버에서 응답이 오면 기본적으로 문자열 (생긴게 json이라면)=> 자바스크립트 오브젝트로 변환
        }).done(function(response){  // 자바스크립트 오브젝트로 변환된 상태로 받음
            if(response.status == 200){
                alert("회원가입이 완료되었습니다. !! ");
                location.href="/"; // 첫화면으로 이동 (여기서는 index.mustache)
            }else{
                alert("회원가입에 실패하였습니다.");
                alert(response.status);
            }
        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    /********
    로그인
    **********/
    login:function(){
        //alert('user의 save()함수 호출됨');
        let data={
            username:$("#username").val(),  // ui의 username태그의 value를 username변수에 바인딩
            password:$("#password").val()
        };

        $.ajax({
            type:"POST",
            url:"/api/user/login",
            data:JSON.stringify(data),  // http body데이터, 위에서 정의한 자바스크립트 오브젝트인 data를 Json 문자열로 변환
            contentType:"application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
            dataType:"json" // 서버에서 응답이 오면 기본적으로 문자열 (생긴게 json이라면)=> 자바스크립트 오브젝트로 변환
        }).done(function(response){  // 자바스크립트 오브젝트로 변환된 상태로 받음
            if(response.status == 200){
                alert("로그인이 완료되었습니다. !! ");
                location.href="/"; // 첫화면으로 이동 (여기서는 index.mustache)
            }else{
                alert(response.status);
                alert(response.data);
            }


        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    }

}

index.init();