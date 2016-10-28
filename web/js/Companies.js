/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(function () {
    $("#btnModificar").on("click", function () {
        $("#frmEditCompany").submit();
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
                $('<option></option>', {text: item.categoryname}).attr('value', item.categoryid).appendTo('#modalCompany #cat');
            });
        }

    }).fail();

    $("#frmcompany").validate({
        lang: 'es',
        rules: {
            companyname: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            neighborhood: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            zipcode: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            city: {
                required: true,
                minlength: 3,
                maxlength: 20,
            },
            country: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            state: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            region: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            street: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            streetnumber: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            phone: {
                //required: true,
                number: true,
            },
            rfc: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            logo: {
                minlength: 3,
                maxlength: 40,
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
            newCompany();
            return false;
        }

    });
    $("#frmEditCompany").validate({
        lang: 'es',
        rules: {
            companyname: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            neighborhood: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            zipcode: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            city: {
                required: true,
                minlength: 3,
                maxlength: 20,
            },
            country: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            state: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            region: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            street: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            streetnumber: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            phone: {
                //required: true,
                number: true,
            },
            rfc: {
                minlength: 3,
                maxlength: 40,
                required: true,
            },
            logo: {
                minlength: 3,
                maxlength: 40,
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
            UpdateCompany();
            return false;
        }

    });
    $('#tbCompanies').DataTable({
        responsive: true,
        ajax: {
            url: 'GetCompanies',
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
            {"data": "companyid"},
            {"data": "companyname"},
            {"data": "neighborhood"},
            {"data": "zipcode"},
            {"data": "city"},
            {"data": "country"},
            {"data": "state"},
            {"data": "region"},
            {"data": "street"},
            {"data": "streetnumber"},
            {"data": "phone"},
            {"data": "rfc"},
            {"data": "logo"},
            {
                "data": function (row) {
                    var str = '<div align="center"><button id="btnEliminar" title="Eliminar" class="btn btn-danger btn-xs" onclick="deleteCompany(\'' + row.companyid + '\');" > <i class="glyphicon glyphicon-remove"></i></button>';
                    str += '<button id="btnGuardar" title="Actualizar" class="btn btn-success btn-xs" onclick="showCompany' + '(\''
                            + row.companyid + '\',\'' + row.companyname + '\',\'' + row.neighborhood + '\',\'' + row.zipcode + '\',\'' + row.city + '\',\''
                            + row.country + '\',\'' + row.state + '\',\'' + row.region + '\',\'' + row.street + '\',\''
                            + row.streetnumber + '\',\'' + row.phone + '\',\'' + row.rfc + '\',\'' + row.logo + '\'  );" ><i class="glyphicon glyphicon-edit"></i></button></div>';
                    return str;
                }
            },
        ]
    });
});
function UpdateCompany() {
    $.ajax({
        url: 'UpdateCompany',
        type: 'post',
        data: $('#frmEditCompany').serialize()
    }).done(function (eljson) {
        if (eljson.code === 200) {
            $('#tbCompanies').dataTable().api().ajax.reload();
            //$('#productname2').val('');
            $('#modalCompany').modal('hide');
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

function showCompany(companyid,companyname, neighborhood, zipcode, city, country, state, region, street, streetnumber, phone, rfc, logo) {
    console.log(companyid+' huehue');
    $('#modalCompany #companyid').val(companyid);
    $('#modalCompany #companyname').val(companyname);
    $('#modalCompany #neighborhood').val(neighborhood);
    $('#modalCompany #zipcode').val(zipcode);
    $('#modalCompany #city').val(city);
    $('#modalCompany #country').val(country);
    $('#modalCompany #state').val(state);
    $('#modalCompany #region').val(region);
    $('#modalCompany #street').val(street);
    $('#modalCompany #streetnumber').val(streetnumber);
    $('#modalCompany #phone').val(phone);
    $('#modalCompany #rfc').val(rfc);
    $('#modalCompany #logo').val(logo);


    $('#modalCompany').modal('show');
}
function deleteCompany(companyid) {
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
                    url: 'DeleteCompany',
                    type: 'post',
                    data: {
                        'companyid': companyid,
                    }
                }).done(function (eljson) {
                    if (eljson.code === 200) {
                        $('#tbCompanies').dataTable().api().ajax.reload();
                        $('#companyname').val('');
                        $('#neighborhood').val('');
                        $('#zipcode').val('');
                        $('#city').val('');
                        $('#country').val('');
                        $('#state').val('');
                        $('#region').val('');
                        $('#street').val('');
                        $('#streetnumber').val('');
                        $('#phone').val('');
                        $('#rfc').val('');
                        $('#logo').val('');
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

function newCompany() {
    $.ajax({
        url: "NewCompany",
        type: "post",
        data: $("#frmcompany").serialize(),
    }).done(function (eljson) {
        console.log(eljson);
        if (eljson.code === 200) {
            $('#tbCompanies').dataTable().api().ajax.reload();
            $('#tbCompanies').dataTable().api().ajax.reload();
            $('#companyname').val('');
            $('#neighborhood').val('');
            $('#zipcode').val('');
            $('#city').val('');
            $('#country').val('');
            $('#state').val('');
            $('#region').val('');
            $('#street').val('');
            $('#streetnumber').val('');
            $('#phone').val('');
            $('#rfc').val('');
            $('#logo').val('');
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