/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function () {
    $("#btnModificar").on("click", function () {
        $("#frmEditUser").submit();
    });
    /////////////////////////COMBO roles
    $.ajax({
        url: 'GetRoles',
        type: 'get',
        dataType: 'json'
    }).done(function (eljason) {
        if (eljason.code == 200) {
            console.log('si entra al json');
            var categories = $.parseJSON(eljason.msg);
            categories.forEach(function (item) {
                $('<option></option>', {text: item.rolename}).attr('value', item.roleid).appendTo('#roleid');
                $('<option></option>', {text: item.rolename}).attr('value', item.roleid).appendTo('#modalUser #roleid');
            });
        }

    }).fail();
    ///////////////////////////COMBO compañias
    $.ajax({
        url: 'GetCompanies',
        type: 'get',
        dataType: 'json'
    }).done(function (eljason) {
        if (eljason.code == 200) {
            console.log('si entra al json');
            var categories = $.parseJSON(eljason.msg);
            categories.forEach(function (item) {
                $('<option></option>', {text: item.companyname}).attr('value', item.companyid).appendTo('#companyid');
                $('<option></option>', {text: item.companyname}).attr('value', item.companyid).appendTo('#modalUser #companyid');
            });
        }

    }).fail();

    $("#frmuser").validate({
        lang: 'es',
        rules: {
            username: {
                minlength: 4,
                maxlength: 40,
                required: true,
            },
            password: {
                minlength: 6,
                maxlength: 40,
                required: true,
            },
            phone: {
                minlength: 6,
                maxlength: 20,
                required: true,
            },
            neigborhood: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            zipcode: {
                required: true,
                minlength: 4,
                maxlength: 10,
            },
            city: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            country: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            state: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            region: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            street: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            email: {
                required: true,
                email: true,
            },
            streetnumber: {
                required: true,
                minlength: 1,
                maxlength: 7,
            },
            photo: {
                //required: true,
                //minlength: 3,
                //maxlength: 20,
                extension:"png"
            },
            cellphone: {
                required: true,
                minlength: 5,
                maxlength: 15,
            },
            gender: {
                required: true,
            },
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function (element) {
            $(element).closest('.form-group').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function (error, element) {
            if (element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        },
        submitHandler: function (form) {
            newUser();
            console.log($('#frmuser').serialize());
            return false;
        }

    });
    $("#frmEditUser").validate({
        lang: 'es',
        rules: {
            username: {
                minlength: 4,
                maxlength: 40,
                required: true,
            },
            password: {
                minlength: 6,
                maxlength: 40,
                required: true,
            },
            phone: {
                minlength: 6,
                maxlength: 20,
                required: true,
            },
            neigborhood: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            zipcode: {
                required: true,
                minlength: 4,
                maxlength: 10,
            },
            city: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            country: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            state: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            region: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            street: {
                required: true,
                minlength: 3,
                maxlength: 50,
            },
            email: {
                required: true,
                email: true,
            },
            streetnumber: {
                required: true,
                minlength: 1,
                maxlength: 7,
            },
            photo: {
                //required: true,
                //minlength: 3,
                //maxlength: 20,
            },
            cellphone: {
                required: true,
                minlength: 5,
                maxlength: 15,
            },
            gender: {
                required: true,
            },
        },
        highlight: function (element) {
            $(element).closest('.form-group').addClass('has-error');
        },
        unhighlight: function (element) {
            $(element).closest('.form-group').removeClass('has-error');
        },
        errorElement: 'span',
        errorClass: 'help-block',
        errorPlacement: function (error, element) {
            if (element.parent('.input-group').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        },
        submitHandler: function (form) {
            UpdateUser();
            return false;
        }

    });
    $('#tbUsers').DataTable({
        responsive: true,
        ajax: {
            url: 'GetUsers',
            type: 'get',
            //dataType: "json",
            contentType: "application/json",
            dataSrc: function (eljson) {
                console.log("UHHLKHLKHLHLKHLKHLH");
                console.log(eljson.msg);
                return $.parseJSON(eljson.msg);
            },
        },
        columns: [
            {"data": "userid"},
            {"data": "username"},
            {"data": "password"},
            {"data": "phone"},
            {"data": "neigborhood"},
            {"data": "zipcode"},
            {"data": "city"},
            {"data": "country"},
            {"data": "state"},
            {"data": "region"},
            {"data": "street"},
            {"data": "email"},
            {"data": "streetnumber"},
            //{"data": "photo"},
            {
              "data":"photo",
              "render":function (data, type, row){
                  return '<img width="50px" height="50px" src="UserImage?image='+data+'" />';
                  
                   
              }
            },
            {"data": "cellphone"},
            {"data": function (element) {
                    return element.companyid.companyname;
                }

            },
            {"data": function (element) {
                    return element.roleid.rolename;
                }

            },
            {"data": "gender"},
            {"data": "apikey"},
            {
                "data": function (row) {
                    var str = '<div align="center"><button id="btnEliminar" title="Eliminar" class="btn btn-danger btn-xs" onclick="deleteUser(\'' + row.userid + '\');" > <i class="glyphicon glyphicon-remove"></i></button>';
                    str += '<button id="btnGuardar" title="Actualizar" class="btn btn-success btn-xs" onclick="showUser' +'(\''
                            + row.userid + '\',\'' + row.username + '\',\'' + row.password + '\',\'' + row.phone + '\',\'' 
                            +row.neigborhood + '\',\'' + row.zipcode + '\',\'' + row.city + '\',\'' + row.country + '\',\'' 
                            +row.state + '\',\'' + row.region + '\',\'' + row.street + '\',\'' + row.email + '\',\''
                            +row.streetnumber + '\',\'' + row.photo + '\',\'' + row.cellphone + '\',\'' + row.companyid.companyid + '\',\''
                            + row.roleid.roleid + '\',\'' + row.gender + '\'  );" ><i class="glyphicon glyphicon-edit"></i></button></div>';
                    return str;
                }
            },
        ]
    });
});
function UpdateUser() {
    var data = new FormData($('#frmEditUser')[0]);
    $.ajax({
        url: 'UpdateUser',
        type: 'post',
        contentType: false,
        processData: false,
        data:  data,
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbUsers').dataTable().api().ajax.reload();
            //$('#productname2').val('');
            $('#modalUser').modal('hide');
            $.growl.notice({
                title: eljson.detail,
                message: "Actualizado",
                location: 'bc',
            });

        } else
            $.growl.error({
                title: eljson.msg,
                message: "Error",
                location: 'bc',
            });
    }).fail();
}

function showUser(userid, username, password, phone,
        neigborhood, zipcode, city, country,
        state, region, street, email, streetnumber,
        photo, cellphone, companyid, roleid, gender) {
    $('#modalUser #userid').val(userid);
    $('#modalUser #username').val(username);
    $('#modalUser #password').val(password);
    $('#modalUser #phone').val(phone);
    $('#modalUser #neigborhood').val(neigborhood);
    $('#modalUser #zipcode').val(zipcode);
    $('#modalUser #city').val(city);
    $('#modalUser #country').val(country);
    $('#modalUser #state').val(state);
    $('#modalUser #region').val(region);
    $('#modalUser #street').val(street);
    $('#modalUser #email').val(email);
    $('#modalUser #streetnumber').val(streetnumber);
    //$('#modalUser #photo').val(photo);
    $('#modalUser #cellphone').val(cellphone);
    $('#modalUser #companyid').val(companyid);
    $('#modalUser #roleid').val(roleid);
    $('#modalUser #gender').val(gender);

    $('#modalUser').modal('show');
}
function deleteUser(userid) {
    bootbox.confirm({
        message: "¿Seguro que desea eliminar el registro?",
        buttons: {
            confirm: {
                label: 'Si',
                className: 'btn-success'
            },
            cancel: {
                label: 'Tal vez',
                className: 'btn-danger'
            }
        },
        callback: function (result) {
            if (result == true)
                $.ajax({
                    url: 'DeleteUser',
                    type: 'post',
                    data: {
                        'userid': userid,
                    }
                }).done(function (eljson) {
                    if (eljson.code === 200) {
                        $('#tbUsers').dataTable().api().ajax.reload();
                        /*$('#productname').val('');
                         $('#brand').val('');
                         $('#cat').val('');
                         $('#code').val('');
                         $('#currency').val('');
                         $('#purchprice').val('');
                         $('#salepricemay').val('');
                         $('#salepricemin').val('');
                         $('#reorderpoint').val('');
                         $('#stock').val('');*/
                        $.growl.notice({
                            title: eljson.detail,
                            message: "Eliminado",
                            location: 'bc',
                        });
                    } else
                        $.growl.error({
                            title: eljson.msg,
                            message: "Error",
                            location: 'bc',
                        });
                }).fail();
        }
    });

}

function newUser() {
    var data = new FormData($('#frmuser')[0]);
    $.ajax({
        url: "CreateUser",
        type: "post",
        contentType: false,
        processData: false,
        data:  data,
    }).done(function (eljson) {
        console.log(eljson.code);
        if (eljson.code === 200) {
            $('#tbUsers').dataTable().api().ajax.reload();
            $('#username').val('');
            $('#password').val('');
            $('#phone').val('');
            $('#neigborhood').val('');
            $('#zipcode').val('');
            $('#city').val('');
            $('#country').val('');
            $('#state').val('');
            $('#region').val('');
            $('#street').val('');
            $('#email').val('');
            $('#streetnumber').val('');
            $('#photo').val('');
            $('#cellphone').val('');
            $('#companyid').val('');
            //$('#modalUser #roleid').val('');
            $('#gender').val('');
            $.growl.notice({
                title: eljson.detail,
                message: "Todo bien",
                location: 'bc',
            });
        } else
            $.growl.error({
                title: eljson.msg+"l",
                message: "Error",
                location: 'bc',
            });
    }).fail();
}