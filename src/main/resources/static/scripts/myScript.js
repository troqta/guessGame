$(document).ready(function () {
    $("#sidenavBtn").on('click', function () {
        $('.sidenav').sidenav();
    });

    const blackTheme = {
        // main icons
        'menu.normalIcon.path': '../css/tui_svg/icon-d.svg',
        'menu.activeIcon.path': '../css/tui_svg/icon-b.svg',
        'menu.disabledIcon.path': '../css/tui_svg/icon-a.svg',
        'menu.hoverIcon.path': '../css/tui_svg/icon-c.svg',
        // submenu icons
        'submenu.normalIcon.path': '../css/tui_svg/icon-d.svg',
        'submenu.activeIcon.path': '../css/tui_svg/icon-c.svg',
    };

    var instance = new tui.ImageEditor(document.querySelector('#tui-image-editor'), {
         includeUI: {
             loadImage: {
                 path: 'http://localhost:8081/upload-dir/test%2012312/n0yvpozzbed21.png',
                 name: 'SampleImage'
             },
             theme: blackTheme,
             initMenu: 'filter',
             menuBarPosition: 'bottom',
             uiSize:{
                width: '100%',
                height: '800px'
             }
         },
        cssMaxWidth: 700,
        cssMaxHeight: 500,
        selectionStyle: {
            cornerSize: 20,
            rotatingPointOffset: 70
        }
    });

    $("#testButton").on('click', function(){
        var dataURL = instance.toDataURL()
        var blob = dataURItoBlob(dataURL);
        var fileOfBlob = new File([blob], 'file.png');
        let formData = new FormData();
        formData.append("file", fileOfBlob);
        fetch('/test', {method: "POST", body: formData});
//        var input = $("<input>")
//                       .attr("type", "file")
//                       .attr("name", "file").val(fileOfBlob);
//        $("#testForm").append(input)
//        $("#testForm").submit()

    });


})

function dataURItoBlob(dataURI) {
    // convert base64/URLEncoded data component to raw binary data held in a string
    var byteString;
    if (dataURI.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(dataURI.split(',')[1]);
    else
        byteString = unescape(dataURI.split(',')[1]);

    // separate out the mime component
    var mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to a typed array
    var ia = new Uint8Array(byteString.length);
    for (var i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ia], {type:mimeString});
}