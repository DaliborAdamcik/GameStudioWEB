function callAjax(url, params, callback){
    callAjaxF(url, params, callback, 'POST');
}

function callAjaxF(url, params, callback, proto){
    var xhr;
    // compatible with IE7+, Firefox, Chrome, Opera, Safari
    xhr = new XMLHttpRequest();
    
    xhr.onreadystatechange = function(){
        if (xhr.readyState == 4 && xhr.status == 200){
            callback(xhr.responseText);
        }
    }
    xhr.open(proto, url, true);
    xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xhr.send(params);
}