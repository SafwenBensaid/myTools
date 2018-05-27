function resizeTextArea() {
    var txt = $('#textAreaManuel');
    var hiddenDiv = $(document.createElement('div'));
    txt.addClass('txtstuff');
    hiddenDiv.addClass('hiddendiv common');
    $('#conteneurMessageTrac').append(hiddenDiv);
    var content = $('#textAreaManuel').val();
    content = content.replace(/\n/g, '<br>');
    hiddenDiv.html(content + '<br class="lbr">');
    $('#textAreaManuel').css('height', hiddenDiv.height());
}

$('#textAreaManuel').keyup(function() {
    resizeTextArea();
});

$(".wikiButton").click(function() {
    var cursorStartPosition = document.getElementById("textAreaManuel").selectionStart;
    var cursorEndPosition = document.getElementById("textAreaManuel").selectionEnd;
    var textAreaTxt = jQuery("#textAreaManuel").val();
    var textStart = "";
    var textEnd = "";
    var cursorposition = 0;
    if ($(this).attr("id") === "strong") {
        textStart = "'''";
        textEnd = "'''";
    } else if ($(this).attr("id") === "em") {
        textStart = "''";
        textEnd = "''";
    } else if ($(this).attr("id") === "heading") {
        textStart = "\n== ";
        textEnd = " ==\n";
    } else if ($(this).attr("id") === "link") {
        textStart = "[";
        textEnd = "]";
    } else if ($(this).attr("id") === "link") {
        textStart = "[";
        textEnd = "]";
    } else if ($(this).attr("id") === "code") {
        textStart = "\n{{{\n";
        textEnd = "\n}}}\n";
    } else if ($(this).attr("id") === "hr") {
        textStart = "\n----\n";
        textEnd = "";
    } else if ($(this).attr("id") === "np") {
        textStart = "\n\n";
        textEnd = "";
    } else if ($(this).attr("id") === "br") {
        textStart = "[[BR]]\n";
        textEnd = "";
    }
    cursorposition = cursorStartPosition + textStart.length;
    $("#textAreaManuel").val(textAreaTxt.substring(0, cursorStartPosition) + textStart + textAreaTxt.substring(cursorStartPosition, cursorEndPosition) + textEnd + textAreaTxt.substring(cursorEndPosition));
    $('#textAreaManuel').selectRange(cursorposition, cursorposition);
    resizeTextArea();
});

$.fn.selectRange = function(start, end) {
    if (!end)
        end = start;
    return this.each(function() {
        if (this.setSelectionRange) {
            this.focus();
            this.setSelectionRange(start, end);
        } else if (this.createTextRange) {
            var range = this.createTextRange();
            range.collapse(true);
            range.moveEnd('character', end);
            range.moveStart('character', start);
            range.select();
        }
    });
};

function ActivateDateTimePicker(fieldId) {
    $('#' + fieldId).datetimepicker({
        formatTime: 'H:i',
        formatDate: 'd-m-Y',
        defaultTime: '08:00',
        format: 'd-m-Y H:i',
        step: 10
    });
}