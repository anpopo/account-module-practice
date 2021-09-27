$(function() {
        $('#btn-delete').on('click', function() {
            deleted();
        });

        function deleted() {
             var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/v1/posts/'+id,
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(posts),
        }).done(function(data, statusText, xhr){
            alert('글이 삭제되었습니다.');
            window.location.href = "/";

        }).fail(function(error) {
            console.log(JSON.stringify(error));
        });

        }

    })