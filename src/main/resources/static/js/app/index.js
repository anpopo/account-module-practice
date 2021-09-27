var main = {
    init : function () {
        var _this = this;

        console.log('12341234');
        $('#btn-save').on('click', function() {
            _this.save();
        });

        $("#btn-update").on('click', function() {
            _this.update();
        });

        $("#btn-delete").on('click', function() {
            _this.deleted();
        });
    },

    save : function () {
        var posts = {
            title: $('#title').val(),
            author: $('#author').val(),
            content: $('#content').val()
        };

        $.ajax({
            type: 'POST',
            url: '/api/v1/posts',
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(posts),
        }).done(function (data) {
            window.location.href = data.headers.location;
        }).fail( function (error){
        });
    },

    update : function () {
        var data = {
            title: $('#title').val(),
            content: $('#content').val()
        };
        var id = $('#id').val();

        $.ajax({
            type: 'PUT',
            url: '/api/v1/posts/'+id,
            dataType: 'JSON',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data)
        }).done(function () {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail( function (error){
            alert(JSON.stringify(error));
        });
    },

    deleted : function () {

     var id = $('#id').val();

     $.ajax({
         type: 'DELETE',
         url: '/api/v1/posts/'+id,
         dataType: 'JSON',
         contentType: 'application/json; charset=utf-8',
     }).done(function () {
         alert('글이 삭제되었습니다.');
         window.location.href = '/';
     }).fail( function (error){
         alert(JSON.stringify(error));
     });
    }
};

main.init();