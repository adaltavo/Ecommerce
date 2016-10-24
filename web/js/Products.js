/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function () {
    $("#btnModificar").on("click", function () {
        $("#frmEditProduct").submit();
    });

    $.ajax({
        url: 'GetCategories',
        type: 'get',
        dataType: 'json'
    }).done(function (eljason) {
        if (eljason.code == 200) {
            console.log('si entra al json');
            var categories = $.parseJSON(eljason.msg);
            categories.forEach(function (item) {
                $('<option></option>', {text: item.categoryname}).attr('value', item.categoryid).appendTo('#cat');
            });
        }

    }).fail();

    $("#frmproduct").validate({
        lang: 'es',
        rules: {
            productname: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            brand: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            cat: {
                required: true,
                number: true,
            },
            code: {
                required: true,
                minlength: 3,
                maxlength: 20,
            },
            currency: {
                required: true,
                minlength: 3,
                maxlength: 3,
            },
            purchprice: {
                //required: true,
                number: true,
            },
            salepricemay: {
                required: true,
                number: true,
            },
            salepricemin: {
                //required: true,
                number: true,
            },
            reorderpoint: {
                //required: true,
                number: true,
            },
            stock: {
                //required: true,
                number: true,
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
            newProduct();
            return false;
        }

    });
    $("#frmEditProduct").validate({
        lang: 'es',
        rules: {
            productname: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            brand: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            cat: {
                required: true,
                number: true,
            },
            code: {
                required: true,
                minlength: 3,
                maxlength: 20,
            },
            currency: {
                required: true,
                minlength: 3,
                maxlength: 3,
            },
            purchprice: {
                //required: true,
                number: true,
            },
            salepricemay: {
                required: true,
                number: true,
            },
            salepricemin: {
                //required: true,
                number: true,
            },
            reorderpoint: {
                //required: true,
                number: true,
            },
            stock: {
                //required: true,
                number: true,
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
            UpdateProduct();
            return false;
        }

    });

    $('#tbProducts').DataTable({
        ajax: {
            url: 'GetProducts',
            type: "POST",
            //dataType: "json",
            contentType: "application/json",
            dataSrc: function (eljson) {
                console.log(eljson.msg);
                return $.parseJSON(eljson.msg);
            },
        },
        columns: [
            {"data": "productid"},
            {"data": "productname"},
            {"data": "brand"},
            {"data": "cat"},
            {"data": "code"},
            {"data": "currency"},
            //{"data": "purchprice"},
            //{"data": "salepricemay"},
            //{"data": "salepricemin"},
            {
                "data": function (element) {
                    return numeral(element.purchprice).format('$0,0.00');
                }
            },
            {
                "data": function (element) {
                    return numeral(element.salepricemay).format('$0,0.00');
                }
            },
            {
                "data": function (element) {
                    return numeral(element.salepricemin).format('$0,0.00');
                }
            },
            {"data": "reorderpoint"},
            {"data": "stock"},
            {
                "data": function (row) {
                    var str = '<div align="center"><button id="btnEliminar" class="btn btn-danger btn-xs" onclick="deleteProduct(\'' + row.productid + '\');" >eliminar</button>';
                    str += '<button id="btnGuardar" class="btn btn-success btn-xs" onclick="showProduct' +
                            '(\''
                            + row.productid + '\',\'' + row.productname + '\',\'' + row.brand + '\',\'' + row.cat + '\' ' +
                            +row.code + '\',\'' + row.currency + '\',\'' + row.purchprice + '\',\'' + row.salepricemay + '\' ' +
                            +row.salepricemin + '\',\'' + row.reorderpoint + '\',\'' + row.stock + '\''
                    '  );" >Actualizar</button></div>';
                    return str;
                }
            },
        ]
    });
});
function updateProduct() {
    $.ajax({
        url: 'UpdateProduct',
        type: 'post',
        data: $('#frmEditProduct').serialize()
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbProducts').dataTable().api().ajax.reload();
            //$('#productname2').val('');
            $('#modalProduct').modal('hide');
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

function showProduct(productid, productname, brand, cat, code, currency, purchprice, salepricemay, salepricemin, reorderpoint, stock) {
    $('#productid').val(productid);
    $('#productname').val(productname);
    $('#brand').val(brand);
    $('#cat').val(cat);
    $('#code').val(code);
    $('#currency').val(currency);
    $('#purchprice').val(purchprice);
    $('#salepricemay').val(salepricemay);
    $('#salepricemin').val(salepricemin);
    $('#reorderpoint').val(reorderpoint);
    $('#stock').val(stock);

    $('#modalProduct').modal('show');
}
function deleteProduct(productid) {
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
                    url: 'DeleteProduct',
                    type: 'post',
                    data: {
                        'productid': productid,
                    }
                }).done(function (eljson) {
                    if (eljson.code === 200) {
                        $('#tbProducts').dataTable().api().ajax.reload();
                        $('#productname').val('');
                        $('#brand').val('');
                        $('#cat').val('');
                        $('#code').val('');
                        $('#currency').val('');
                        $('#purchprice').val('');
                        $('#salepricemay').val('');
                        $('#salepricemin').val('');
                        $('#reorderpoint').val('');
                        $('#stock').val('');
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

function newProduct() {
    $.ajax({
        url: "NewProduct",
        type: "post",
        data: $("#frmproduct").serialize(),
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbProducts').dataTable().api().ajax.reload();
            $('#tbProducts').dataTable().api().ajax.reload();
            $('#productname').val('');
            $('#brand').val('');
            $('#cat').val('');
            $('#code').val('');
            $('#currency').val('');
            $('#purchprice').val('');
            $('#salepricemay').val('');
            $('#salepricemin').val('');
            $('#reorderpoint').val('');
            $('#stock').val('');
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