/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function () {
    $("#btnModificar").on("click", function () {
        $("#frmEditRole").submit();
    });

    $.ajax({
        url: 'GetRoles',
        type: 'get',
        dataType: 'json'
    }).done(function (eljason) {
        if (eljason.code == 200){
            console.log('si entra al json');
            var roles=$.parseJSON(eljason.msg);
            roles.forEach(function (item){
                $('<option></option>',{text:item.rolename}).attr('value',item.roleid).appendTo('#cbRoles');
            });
        }
            
    }).fail();

    $("#frmrole").validate({
        lang: 'es',
        rules: {
            rolename: {
                minlength: 3,
                maxlength: 20,
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
            newRole();
            return false;
        }

    });
    $("#frmEditRole").validate({
        lang: 'es',
        rules: {
            rolename: {
                minlength: 3,
                maxlength: 20,
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
            updateRole();
            return false;
        }

    });

    $('#tbRoles').DataTable({
        ajax: {
            url: 'GetRoles',
            type: "POST",
            //dataType: "json",
            contentType: "application/json",
            dataSrc: function (eljson) {
                console.log(eljson.msg);
                return $.parseJSON(eljson.msg);
            },
        },
        columns: [
            {"data": "roleid"},
            {"data": "rolename"
            },
            {
                "data": function (row) {
                    var str = '<div align="center"><button id="btnEliminar" title="Eliminar" class="btn btn-danger btn-xs" onclick="deleteRole(\'' + row.roleid + '\');" ><i class="glyphicon glyphicon-remove"></i></button>';
                    str += '<button id="btnGuardar" class="btn btn-success btn-xs" title="Actualizar" onclick="showRole(\'' + row.roleid + '\',\'' + row.rolename + '\');" ><i class="glyphicon glyphicon-edit"></i></button></div>';
                    return str;
                }
            },
            {
                "data": function (element) {
                    return numeral(element.roleid).format('$0,0.00');
                }
            }
        ]
    });
});
function updateRole() {
    $.ajax({
        url: 'UpdateRole',
        type: 'post',
        data: $('#frmEditRole').serialize()
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbRoles').dataTable().api().ajax.reload();
            //$('#rolename2').val('');
            $('#modalRole').modal('hide');
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

function showRole(roleid, rolename) {
    $('#roleid').val(roleid);
    $('#rolename2').val(rolename);
    $('#modalRole').modal('show');
}
function deleteRole(roleid) {
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
                    url: 'DeleteRole',
                    type: 'post',
                    data: {
                        'roleid': roleid,
                    }
                }).done(function (eljson) {
                    if (eljson.code === 200) {
                        $('#tbRoles').dataTable().api().ajax.reload();
                        $('#rolename').val('');
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

function newRole() {
    $.ajax({
        url: "CreateRole",
        type: "post",
        data: $("#frmrole").serialize(),
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbRoles').dataTable().api().ajax.reload();
            $('#rolename').val('');
            $.growl.notice({
                title: eljson.detail,
                message: "Todo bien",
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