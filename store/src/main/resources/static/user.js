$(document).ready(function(){
    $.ajax({
        url: "https://localhost:8080/store/user"
    }).then(function (data){
        $('.firstname').append(data.firstname);
        $('.lastname').append(data.lastname);
        $('.age').append(data.age);
    });
})