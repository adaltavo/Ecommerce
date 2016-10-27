/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function () {
    $("#btnModificar").on("click", function () {
        $("#frmEditCategory").submit();
    });

    $.ajax({
        url: 'GetCategories',
        type: 'get',
        dataType: 'json'
    }).done(function (eljason) {
        if (eljason.code == 200){
            console.log('si entra al json');
            var categories=$.parseJSON(eljason.msg);
            categories.forEach(function (item){
                $('<option></option>',{text:item.cateoryname}).attr('value',item.categoryid).appendTo('#cbCategories');
            });
        }
            
    }).fail();

    $("#frmcategories").validate({
        lang: 'es',
        rules: {
            categoryname: {
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
            newCategory();
            return false;
        }

    });
    $("#frmEditCategory").validate({
        lang: 'es',
        rules: {
            categoryname: {
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
            updateCategory();
            return false;
        }

    });

    $('#tbCategories').DataTable({
        ajax: {
            url: 'GetCategories',
            type: "get",
            //dataType: "json",
            contentType: "application/json",
            dataSrc: function (eljson) {
                console.log(eljson.msg);
                return $.parseJSON(eljson.msg);
            },
        },
        columns: [
            {"data": "categoryid"},
            {"data": "categoryname"
            },
            {
                "data": function (row) {
                    var str = '<div align="center"><button id="btnEliminar" title="Eliminar" class="btn btn-danger btn-xs" onclick="deleteCategory(\'' + row.categoryid + '\');" ><i class="glyphicon glyphicon-remove"></i></button>';
                    str += '<button id="btnGuardar" class="btn btn-success btn-xs" title="Actualizar" onclick="showCategory(\'' + row.categoryid + '\',\'' + row.categoryname + '\');" ><i class="glyphicon glyphicon-edit"></i></button></div>';
                    return str;
                }
            },
        ]
    });
});
function updateCategory() {
    $.ajax({
        url: 'UpdateCategory',
        type: 'post',
        data: $('#frmEditCategory').serialize()
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbCategories').dataTable().api().ajax.reload();
            //$('#rolename2').val('');
            $('#modalCategory').modal('hide');
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

function showCategory(categoryid, categoryname) {
    $('#categoryid').val(categoryid);
    $('#categoryname2').val(categoryname);
    $('#modalCategory').modal('show');
}
function deleteCategory(categoryid) {
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
                    url: 'DeleteCategory',
                    type: 'post',
                    data: {
                        'categoryid': categoryid,
                    }
                }).done(function (eljson) {
                    if (eljson.code === 200) {
                        $('#tbCategories').dataTable().api().ajax.reload();
                        $('#categoryname').val('');
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

function newCategory() {
    $.ajax({
        url: "NewCategory",
        type: "post",
        data: $("#frmcategories").serialize(),
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbCategories').dataTable().api().ajax.reload();
            $('#categoryname').val('');
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