/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

$(function () {
    $('#nav a').on('click',function(event){
        event.preventDefault();
        var page=$(this).attr('href');
        $('#content').load(page,{'param':'desdejavascript'});
        $('#nav li').removeClass('active');
        $(this).parent().addClass('active');
    });
    
});