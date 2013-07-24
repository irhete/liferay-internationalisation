function deleteRow(index, key, locale, actionUrl) {
	$.ajax({  
        type : 'POST',  
        url : actionUrl+"&key="+key+"&locale="+locale,  

    });
//	$("#row"+index).remove();
}

/*
function addRow(currentLocale, deleteTranslationMethodURL, deleteTranslation) {
	var key = $("#newKey").val();
	var value = $("#newValue").val();
	var lastId = $('#translationTable tr:last').attr('id');
	var newIndex = parseInt(lastId.substring(3, lastId.length)) + 1;
	var newRow = $("<tr id='row"+newIndex+"'></tr>");
	newRow.append("<td><input name='"+key+"' type='text' value='"+key+"'/></td>"); 
	newRow.append("<td><input name='"+key+"Value' type='text' value='"+value+"'/></td>"); 
	newRow.append("<td><button onclick=\"javascript:deleteRow('"+newIndex+"', '"+key+"', '"+currentLocale+"', '"+deleteTranslationMethodURL+"');\">"+deleteTranslation+"</button></td>"); 
	$("#translationTable tr:last").after(newRow);
}

$(document).ready(function(){
    $("#addTranslationForm").submit(function() {
        $.post($("#addTranslationForm").attr("action"), $("#addTranslationForm").serialize());
        $("#newKey").val("");
        $("#newValue").val("");
        return false;
    });
});

$(document).ready(function(){
    $("#updateTranslationsForm").submit(function() {
        $.post($("#updateTranslationsForm").attr("action"), $("#updateTranslationsForm").serialize());
        return false;
    });
});
*/