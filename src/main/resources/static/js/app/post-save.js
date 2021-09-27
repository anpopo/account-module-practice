$(function() {
        $('#btn-save').on('click', function() {
            save();
        });

        function save() {
            var posts = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(posts),
        }).done(function(data, statusText, xhr){
            alert('글 등록이 완료되었습니다.');
            window.location.href = "/";

        }).fail(function(error) {
            console.log(JSON.stringify(error));
        });

        }

    })