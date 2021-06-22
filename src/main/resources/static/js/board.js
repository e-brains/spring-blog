let index={
    init:function(){ // 리스너 등록
        $("#btn-board-save").on("click", ()=>{   // function(){}을 사용하지 않은 이유는 this를 바인딩하기 위해서 !!
            this.save();  //  function(){}을 사용하면 this가 윈도우 객체를 가리키게 되기 때문에 ()=>{}을 사용함
        });

        $("#btn-board-delete").on("click", ()=>{   // 상세화면에서 글 삭제
            this.deleteById();
        });

        $("#btn-board-update").on("click", ()=>{   // 상세화면에서 글 수정
            this.update();
        });

        $("#btn-reply-save").on("click", ()=>{   // 상세화면에서 댓글 쓰기
            this.replySave();
        });
    },

    /********
    글 저장
    **********/
    save:function(){
        alert("글저장 시작 -=======")
        let data={
            title:$("#title").val(),
            content:$("#content").val()
        };

        $.ajax({
            type:"POST",
            url:"/api/board/saveForm",
            data:JSON.stringify(data),  // http body데이터, 위에서 정의한 자바스크립트 오브젝트인 data를 Json 문자열로 변환
            contentType:"application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
            dataType:"json" // 서버에서 응답이 오면 기본적으로 문자열 (생긴게 json이라면)=> 자바스크립트 오브젝트로 변환
        }).done(function(response){  // 자바스크립트 오브젝트로 변환된 상태로 받음
            if(response.status == 200){
                alert("글쓰기가 완료되었습니다. !! ");
                location.href="/"; // 첫화면으로 이동 (여기서는 index.mustache)
            }else{
                alert("글쓰기 실패 ");
                alert(response.status);
            }

        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    /********
    글 수정
    **********/
    update:function(){
        alert("글수정 시작 -=======")
        let data={
            id:$("#boardId").val(),
            title:$("#title").val(),
            content:$("#content").val()
        };

        $.ajax({
            type:"PUT",
            url:"/api/board/modify",
            data:JSON.stringify(data),  // http body데이터, 위에서 정의한 자바스크립트 오브젝트인 data를 Json 문자열로 변환
            contentType:"application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
            dataType:"json" // 서버에서 응답이 오면 기본적으로 문자열 (생긴게 json이라면)=> 자바스크립트 오브젝트로 변환
        }).done(function(response){  // 자바스크립트 오브젝트로 변환된 상태로 받음
            if(response.status == 200){
                alert("글수정이 완료되었습니다. !! ");
                location.href="/"; // 첫화면으로 이동 (여기서는 index.mustache)
            }else{
                alert("글수정 실패 ");
                alert(response.status);
            }

        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    /********
    글 삭제
    **********/
    deleteById:function(){
        alert("글삭제 시작 -=======");
        let id = $("#boardId").text();

        $.ajax({
            type:"DELETE",
            url:"/api/board/deleteById/"+id,
            dataType:"json" // 서버에서 응답이 오면 기본적으로 문자열 (생긴게 json이라면)=> 자바스크립트 오브젝트로 변환
        }).done(function(response){  // 자바스크립트 오브젝트로 변환된 상태로 받음
            if(response.status == 200){
                alert("삭제가 완료되었습니다. !! ");
                location.href="/"; // 첫화면으로 이동 (여기서는 index.mustache)
            }else{
                alert("글삭제 시 오류 ");
                alert(response.status);
            }

        }).fail(function(error){
            alert(JSON.stringify(error));
        });
    },

    /********
    댓글 저장 (DTO 미사용)
    **********/
//    replySave:function(){
//        alert("댓글 저장 시작 -=======")
//        let data={
//            content:$("#reply-content").val()
//        };
//
//        let boardId = $("#boardId").text();
//
//        console.log(boardId);
//
//        $.ajax({
//            type:"POST",
//            url:`/api/board/${boardId}/reply`, // 싱글 쿼테이션이 아님 숫자키 앞에 ` 임
//            data:JSON.stringify(data),  // http body데이터, 위에서 정의한 자바스크립트 오브젝트인 data를 Json 문자열로 변환
//            contentType:"application/json; charset=utf-8", // body데이터가 어떤 타입인지(MIME)
//            dataType:"json" // 서버에서 응답이 오면 기본적으로 문자열 (생긴게 json이라면)=> 자바스크립트 오브젝트로 변환
//        }).done(function(response){  // 자바스크립트 오브젝트로 변환된 상태로 받음
//            if(response.status == 200){
//                alert("댓글 쓰기가 완료되었습니다. !! ");
//                location.href=`/board/detail/${boardId}`; // 다시 상세 화면으로 이동
//            }else{
//                alert("댓글 쓰기 실패 ");
//                alert(response.status);
//            }
//
//        }).fail(function(error){
//            alert(JSON.stringify(error));
//        });
//    }

        /********
        댓글 저장 (DTO 사용)
        **********/
        replySave:function(){
            alert("댓글 저장 시작 DTO 사용 -=======")
            let data={
                userId: $("#userId").val(),
            	boardId: $("#boardId").text(),
                content:$("#reply-content").val()
            };

            console.log(data);

            $.ajax({
                type:"POST",
                url:`/api/board/${data.boardId}/replyDto`, // 싱글 쿼테이션이 아님 숫자키 앞에 ` 임
                data:JSON.stringify(data),
                contentType:"application/json; charset=utf-8",
                dataType:"json"
            }).done(function(response){
                if(response.status == 200){
                    alert("댓글 쓰기가 완료되었습니다. !! ");
                    location.href=`/board/detail/${data.boardId}`; // 다시 상세 화면으로 이동
                }else{
                    alert("댓글 쓰기 실패 ");
                    alert(response.status);
                }

            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        },

        /********
        댓글 삭제
        (리스너가 아니고 onClick 함수이기 때문에 맨 앞단에서의 리스너 등록이 필요 없음)
        여기서 boardId는 특별한 용도는 없고 단순히 주소를 만들기 위해서 받음
        **********/
        replyDelete:function(boardId, principalId, userId, replyId){
            alert("댓글 삭제 시작 -=======");
            console.log(replyId);

            if ( principalId != userId){   // 로그인한 자기 것만 삭제 가능하게 한다.
                alert("삭제할 권한이 없습니다.");
                return
            }

            $.ajax({
                type:"DELETE",
                url:`/api/board/${boardId}/reply/${replyId}`, // 싱글 쿼테이션이 아님 숫자키 앞에 ` 임
                dataType:"json"
            }).done(function(response){
                if(response.status == 200){
                    alert("댓글 삭제가 완료되었습니다. !! ");
                    location.href=`/board/detail/${boardId}`; // 다시 상세 화면으로 이동
                }else{
                    alert("댓글 삭제 실패 ");
                    alert(response.status);
                }

            }).fail(function(error){
                alert(JSON.stringify(error));
            });
        }

}

index.init();