$(document).ready(function() {
    $("#sidenavBtn").on('click', function() {
        $('.sidenav').sidenav();
    });
    $('.dropdown-trigger').dropdown();
    $('.dropify').dropify();
    $('.tooltipped').tooltip();
    $('#tabs').tabs();

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
    var instance = null;
    //    var instance = new tui.ImageEditor(document.querySelector('#tui-image-editor'), {
    //         includeUI: {
    //             loadImage: {
    //                 path: 'http://localhost:8081/upload-dir/test%2012312/n0yvpozzbed21.png',
    //                 name: 'SampleImage'
    //             },
    //             theme: blackTheme,
    //             initMenu: 'filter',
    //             menuBarPosition: 'bottom',
    //             uiSize:{
    //                width: '100%',
    //                height: '800px'
    //             }
    //         },
    //        cssMaxWidth: 700,
    //        cssMaxHeight: 500,
    //        selectionStyle: {
    //            cornerSize: 20,
    //            rotatingPointOffset: 70
    //        }
    //    });
    //
    //    $("#testButton").on('click', function(){
    //        var dataURL = instance.toDataURL()
    //        var blob = dataURItoBlob(dataURL);
    //        var fileOfBlob = new File([blob], 'file.png');
    //        let formData = new FormData();
    //        formData.append("file", fileOfBlob);
    //        fetch('/test', {method: "POST", body: formData});
    ////        var input = $("<input>")
    ////                       .attr("type", "file")
    ////                       .attr("name", "file").val(fileOfBlob);
    ////        $("#testForm").append(input)
    ////        $("#testForm").submit()
    //
    //    });

    $('#userEditPictureBtn').on('click', function() {
        var imagePath = $('#oldImg').attr('src')
        instance = new tui.ImageEditor(document.querySelector('#tui-image-editor'), {
            includeUI: {
                loadImage: {
                    path: imagePath,
                    name: 'SampleImage'
                },
                theme: blackTheme,
                initMenu: 'filter',
                menuBarPosition: 'bottom',
                uiSize: {
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
        $('#userEditPictureBtn').css('display', 'none')
    });


    $('#userEditSubmitBtn').on('click', function() {
        let formData = new FormData();
        let updatedPicture = ($('#tui-image-editor').children().length > 0)

        if (!$('#password').val() && !$('#email').val() && !updatedPicture) {
            M.toast({
                html: 'No changes made!'
            })
            return;
        }


        if ($('#password').val()) {
            if (!$('#repeatPassword').val() || !$('#oldPassword').val()) {
                M.toast({
                    html: 'Repeat password or old password missing!'
                })
                console.log($('#password').val())
                console.log($('#repeatPassword').val())
                console.log($('#oldPassword').val())
                return;
            }
            if ($('#password').val() !== $('#repeatPassword').val()) {
                M.toast({
                    html: 'Repeat password doesn\'t match!'
                })
                return;
            }
            formData.append("password", $('#password').val());
            formData.append("oldPassword", $('#oldPassword').val());
        }

        if ($('#email').val()) {
            var testEmail = /^[A-Z0-9._%+-]+@([A-Z0-9-]+\.)+[A-Z]{2,4}$/i;

            if (!testEmail.test($('#email').val())) {
                M.toast({
                    html: 'Invalid email!'
                })
                return;
            }

            formData.append('email', $('#email').val());
        }
        if (updatedPicture) {
            var dataURL = instance.toDataURL()
            var blob = dataURItoBlob(dataURL);
            var fileOfBlob = new File([blob], 'file.png');
            formData.append("file", fileOfBlob);
        }
        var xhttp = new XMLHttpRequest();
        xhttp.open('POST', '/api/user/edit/', true);
        xhttp.onload = function() {
            console.log(xhttp.responseText)
            response = JSON.parse(xhttp.responseText)
            console.log(response.statusCode)
            console.log(response.body)

            if (response.statusCode !== 200) {
                response.body.forEach(function(error) {
                    $('#errors').append('<small style="color:rgba(255, 0 , 0, 0.5)">' + error + '</small>')
                })
            } else {
                window.location = '/user/profile'
            }
        }
        xhttp.send(formData);

        //        fetch('/api/user/edit', {method: "POST", body: formData})
        //        .then(function(data) {
        //            console.log(data.json())
        //        })
        //        .catch(function(error){
        //            console.log(error)
        //        })
    });
    if ($('#postCreatePageFlag').length) {
        instance = new tui.ImageEditor(document.querySelector('#tui-image-editor'), {
            includeUI: {
                loadImage: {
                    path: '/css/default/default_post.jpg',
                    name: 'SampleImage'
                },
                theme: blackTheme,
                initMenu: 'filter',
                menuBarPosition: 'bottom',
                uiSize: {
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
    }

    $('#gagCreateBtn').on('click', function() {
        let formData = new FormData();
        if (!$('#title').val()) {
            M.toast({
                html: 'A title is required to create a post!'
            })
            return;
        } else {
            if ($('#title').val().length < 5 || $('#title').val().length > 20) {
                M.toast({
                    html: 'The title must be between 5 and 20 symbols long!'
                })
                return;
            }
            formData.append('title', $('#title').val());
        }

        if (!$('#answer').val()) {
            M.toast({
                html: 'An answer is required to create a post!'
            })
            return;
        } else {
            if ($('#title').val().length < 5 || $('#title').val().length > 20) {
                M.toast({
                    html: 'The answer must be between 5 and 20 symbols long!'
                })
                return;
            }
            formData.append('answer', $('#answer').val());
        }

        if (!$('#description').val()) {
            formData.append('description', $('#description').val());
        }
        var dataURL = instance.toDataURL()
        var blob = dataURItoBlob(dataURL);
        var fileOfBlob = new File([blob], 'file.png');
        formData.append("file", fileOfBlob);
        var xhttp = new XMLHttpRequest();
        xhttp.open('POST', '/api/post/create', true);
        xhttp.onload = function() {
            console.log(xhttp.responseText)
            response = JSON.parse(xhttp.responseText)
            console.log(response.statusCode)
            console.log(response.body)

            if (response.statusCode !== 200) {
                response.body.forEach(function(error) {
                    $('#errors').append('<small style="color:rgba(255, 0 , 0, 0.5)">' + error + '</small>')
                })
            } else {
                window.location = '/'
            }
        }
        xhttp.send(formData);

    })
    if ($('#registerForm').length) {
        $('#username').change(function() {
            let username = $('#username').val()
            var xhttp = new XMLHttpRequest();
            xhttp.open('POST', '/api/user/availabilityCheck/' + username, true);
            xhttp.onload = function() {
                console.log(xhttp.responseText)
                response = JSON.parse(xhttp.responseText)
                console.log(response.statusCode)
                console.log(response.body)

                if (response.statusCode !== 200) {
                    $('#username').addClass('invalid')
                    $('#username').removeClass('valid')
                    M.toast({
                        html: 'Username is unavailable!'
                    })
                } else {
                    $('#username').addClass('valid')
                    $('#username').removeClass('invalid')
                    M.toast({
                        html: 'Username is available'
                    })
                }
            }
            xhttp.send();

        });
    }
    $('.myBanClass').on('click', banFunction);
    $('.myUnbanClass').on('click', unbanFunction);
    $('.myMakeAdminClass').on('click', makeAdminFunction);
    $('.myRemoveAdminClass').on('click', removeAdminFunction);
    var button;

    function banFunction() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".id")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to ban user with id " + itemText);
        $('#confirmButton').on('click', function() {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/api/user/ban/" + $item.text(), true);
            xhttp.onload = function() {
                if (JSON.parse(xhttp.responseText).statusCode === 200) {
                    M.toast({
                        html: "Ban successful!"
                    })
                    $item.parent().css('background-color', 'rgba(255, 0, 0, 0.5)');

                    button.prop("onclick", null).off("click");
                    button.html('UNBAN');
                    button.on('click', unbanFunction);
                } else {
                    M.toast({
                        html: JSON.parse(xhttp.responseText).body
                    })
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });
        $('.modal').modal();
    };

    function unbanFunction() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".id")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to unban user with id" + itemText);
        $('#confirmButton').on('click', function() {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/api/user/unBan/" + $item.text(), true);
            var flag = false;
            xhttp.onload = function() {

                if (JSON.parse(xhttp.responseText).statusCode === 200) {
                    M.toast({
                        html: "Unban successful"
                    })
                    $item.parent().css('background-color', 'rgba(0, 255, 0, 0.5)');
                    button.prop("onclick", null).off("click");
                    button.html('BAN');
                    button.on('click', banFunction);
                } else {
                    M.toast({
                        html: JSON.parse(xhttp.responseText).body
                    })
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });

        $('.modal').modal();
    };

    function makeAdminFunction() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".id")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to give admin rights to user with id " + itemText);
        $('#confirmButton').on('click', function() {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/api/user/makeAdmin/" + $item.text(), true);
            xhttp.onload = function() {
                if (JSON.parse(xhttp.responseText).statusCode === 200) {
                    M.toast({
                        html: "User is now an admin!"
                    })

                    button.prop("onclick", null).off("click");
                    button.html('Remove Admin');
                    button.on('click', removeAdminFunction);
                } else {
                    M.toast({
                        html: JSON.parse(xhttp.responseText).body
                    })
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });
        $('.modal').modal();
    };

    function removeAdminFunction() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".id")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to retract admin rights from user with id" + itemText);
        $('#confirmButton').on('click', function() {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/api/user/removeAdmin/" + $item.text(), true);
            var flag = false;
            xhttp.onload = function() {

                if (JSON.parse(xhttp.responseText).statusCode === 200) {
                    M.toast({
                        html: "User is no longer an admin"
                    })
                    button.prop("onclick", null).off("click");
                    button.html('Make Admin');
                    button.on('click', makeAdminFunction);
                } else {
                    M.toast({
                        html: JSON.parse(xhttp.responseText).body
                    })
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });

        $('.modal').modal();
    };
    $('.makeOwnerButton').on('click', function() {
        button = $(this);
        $item = $(this).closest("tr")
            .find(".id")
        itemText = $item.text();
        $('#userPlaceholder').html("Are you sure you wish to give up your owner rights and hand them over to user with id " + itemText);
        $('#confirmButton').on('click', function() {
            var xhttp = new XMLHttpRequest();
            xhttp.open("POST", "/api/user/giveOwner/" + $item.text(), true);
            var flag = false;
            xhttp.onload = function() {

                if (JSON.parse(xhttp.responseText).statusCode === 200) {
                    window.location = "/logout"
                } else {
                    M.toast({
                        html: JSON.parse(xhttp.responseText).body
                    })
                }
            }
            $('#confirmButton').prop("onclick", null).off("click");
            xhttp.send();
        });

        $('.modal').modal();

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

    return new Blob([ia], {
        type: mimeString
    });
}