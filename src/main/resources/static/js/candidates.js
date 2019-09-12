$("#c-dob").datepicker({autoclose: true,});
$("#from-date").datepicker({autoclose: true,});
$("#to-date").datepicker({autoclose: true,});
$("#c-fulltime").datepicker({autoclose: true,});
$("#c-graduation").datepicker({autoclose: true,});
$("#new-candidate-dob").datepicker({autoclose: true,});
$("#new-candidate-graduation").datepicker({autoclose: true,});
$("#new-candidate-fulltime").datepicker({autoclose: true,});
var candidate = $("#candidate-table").DataTable({"lengthMenu":[[20,40,100,250],[20,40,100,250]],"scrollY": 1200,"scrollX": true,"order":[[1,"asc"]],});
var eventsList = $("#event-attend-list").DataTable({"scrollX": true,"order":[[1,"asc"]],});
var errorTable = $("#upload-errors").DataTable({"scrollX": true,"order":[[0,"asc"]],});
$(candidate.table().header()).addClass('hightlight');
var counter1 = 0;
var counter2 = 0;

$("#import-btn").click(function(event){
    $(this).prop('disabled',true);
    event.preventDefault();
    submitForm();
    $(this).prop('disabled',false);
});
function submitForm(){
    var form = $("#import-form")[0];
    var data = new FormData(form);
    var message = "";
    $("#import-btn").prop("disable",true);
    $.ajax({
            type: "POST",
            enctype: 'multipart/form-data',
            url: "/manage/upload",
            data: data,
            processData: false,
            contentType: false,
            cache: false,
            timeout: 1000000,
            success: function(data, textStatus, jqXHR) {
                message = "<div class='alert alert-success'>"+data+"</div>";
                $("#result").html(message);
                console.log("SUCCESS : ", data);
                $("#import-btn").prop("disabled", false);
                $('#import-form')[0].reset();
                window.location.href = "/manage/candidates";
            },
            error: function(jqXHR, textStatus, errorThrown) {
                message = "<div class='alert alert-danger'>Error</div>"
                $("#result").html(message);
                console.log("ERROR : ", jqXHR.responseText);
                $("#import-form").prop("disabled", false);
            },
        });
};
$("#export-btn").on('click',function(){
    var fileName = $("#export-filename").val()+".xlsx";
    var target = $("#export-message");
    var list = candidate.column(1).data().toArray();
    var message = "";
    var intList = "";
    for(i=0;i<list.length;i++){
        intList+=parseInt(list[i],10)+"+";
    }
    if(fileName!=null){
        $.ajax({
                type: "GET",
                url: "/manage/candidates/export",
                data: {
                    file: fileName,
                    candidate: intList,
                },
                success: function(data){
                    window.location.href = "/manage/download?fileName="+fileName;
                },
                error: function(e){
                    console.log(e);
                },
                complete: function(){
                    $("#export-modal").modal('toggle');
                }
            });
    }

});
$("#filter").on('click',function(){
    var universities = $("#filter-university").val();
    var skills = $("#filter-skill").val();
    var statuses = $("#filter-status").val();
    var events = $("#filter-event").val();
    $.ajax({
        type: "GET",
        url: "/manage/candidates/filter",
        data:{
            universities : universities,
            skills: skills,
            statuses: statuses,
            events: events,
        },
        success: function(data){
            var res = jQuery.parseJSON(data);
            console.log(res);
            candidate.clear().draw(false);
            jQuery(res).each(function(i,item){
                var irow = [];
                var b2id = "edit-candidate-"+item.id;
                var b3id = "delete-candidate-"+item.id;
                var button2 = '<button type="button" class="btn btn-success btn-small ml-2 candidate-edit" data-toggle="modal" data-target="#candidate-information" id=\"'+b2id+'\"><i class="fas fa-user-edit"></i></button>';
                var button3 = '<button type="button" class="btn btn-danger btn-small ml-2 candidate-delete" data-toggle="modal" data-target="#candidate-delete" id=\"'+b3id+'\"><i class="fas fa-user-times"></i></button>';
                var cell = button2+button3;
                irow.push(cell);
                irow.push(item.id);
                irow.push(item.name);
                irow.push(item.gender);
                irow.push(item.university.name);
                irow.push(item.faculty.name);
                irow.push(item.email);
                irow.push(item.birthday);
                irow.push(item.skill.name);
                candidate.row.add(irow).draw();
            });
        },
        error: function(e){
            console.log(e);
        },
    })
})
$("tbody[id=candidate-list]").on('click','.candidate-delete[id^=delete-candidate]',function(){
    var s = $(this).attr('id');
    var id = s.split("-")[2];
    $("#candidate-delete-id").text(id);
});
$("#delete-confirm").on('click',function(){
    var id = $("#candidate-delete-id").text();
    $.ajax({
        type: "GET",
        url: "/manage/candidates/delete",
        data: {
            id: id,
        },
        success: function(data){
            console.log(data);
            $("#candidate-delete").modal('toggle');
            var irow = $("#delete-candidate-"+id).parents('tr');
            candidate.row(irow).remove().draw(false);
        },
        error: function(e){
            console.log(e);
        }
    });
});
$("#cadidate-delete").on('hidden.bs.modal',function(){
    $("#candidate-delete-id").text("");
});
$("tbody[id=candidate-list]").on('click','.candidate-edit[id^=edit-candidate]',function(){
    var s = $(this).attr('id');
    var id = s.split("-")[2];
    $.ajax({
        type: "GET",
        url: "/manage/candidates/get",
        data: {
            id: id,
        },
        success: function(data){
            var res = jQuery.parseJSON(data);
            console.log(res);
            $("#candidate-name-big").text(res.name);
            $("#candidate-gender").text(res.gender);
            $("#candidate-skill").text(res.skill.name);
            $("#candidate-university").text(res.university.code);
            $("#candidate-gender1").text(res.gender);
            $("#candidate-name").text(res.name);
            $("#candidate-dob").text(toDate(res.birthday));
            $("#candidate-email").text(res.email);
            $("#candidate-phone").text(res.phone);
            $("#candidate-facebook").text(res.facebook);
            $("#candidate-skill").text(res.skill.name);
            $("#candidate-university").text(res.university.code);
            $("#candidate-unv").text(res.university.name);
            $("#candidate-gender1").text(res.gender);
            $("#candidate-faculty").text(res.faculty.code+"("+res.faculty.name+")");
            $("#candidate-grade").text(res.gpa);
            $("#candidate-graduation").text(toDate(res.graduationDate));
            $("#candidate-fulltime").text(toDate(res.fulltime));
            $("#c-name").val(res.name);
            if(res.gender=="Male"){
                $("#gender-male").attr('checked',true);
            }else{
                $("#gender-female").attr('checked',true);
            }
            if(res.birthday=="null"){
                $("#c-dob").val("");
            }else{
                $("#c-dob").val(toDate(res.birthday));
            }
            $("#candidate-id").text(res.id);
            $("#c-university").val(res.university.name);
            $("#c-faculty").val(res.faculty.code+"("+res.faculty.name+")");
            $("#c-grade").val(res.gpa);
            $("#c-skill").val(res.skill.name);
            $("#c-account").val(res.account);
            $("#c-email").val(res.email);
            $("#c-phone").val(res.phone);
            $("#c-facebook").val(res.facebook);
            if(res.fulltimeDate=="null"){
                $("#c-fulltime").val("");
            }else{
                $("#c-fulltime").val(toDate(res.fulltimeDate));
            }
            if(res.graduationDate=="null"){
                $("#c-graduation").val("");
            }else{
                $("#c-graduation").val(toDate(res.graduationDate));
            }
            getEvents(res.id,eventsList);
        },
        error: function(e){
            console.log(e);
        }
    });
});
$("#edit-candidate-button").on('click',function(){
    $("#candidate-information").modal('toggle');
    setTimeout(function(){
        $("#candidate-edit").modal('show');
    },500);
});
$("#save-edit").on('click',function(){
    var id = $("#candidate-id").text();
    var name = $("#c-name").val();
    var gender = "";
    if($("#gender-male").is(":checked")){
        gender = "Male";
    }else{
        gender = "Female";
    }
    var dob= $("#c-dob").val();
    var uv = $("#c-university").val();
    var faculty = $("#c-faculty").val();
    var grade = $("#c-grade").val();
    var status = $("#c-status").val();
    var skill = $("#c-skill").val();
    var account = $("#c-account").val();
    var email = $("#c-email").val();
    var phone = $("#c-phone").val();
    var fb = $("#c-facebook").val();
    var graduation = $("#candidate-graduation").val();
    var fulltime = $("#candidate-fulltime").val();
    $.ajax({
        type: "GET",
        url: "/manage/candidates/edit",
        data: {
            id: id,
            name: name,
            gender: gender,
            dob: dob,
            uv: uv,
            faculty: faculty,
            grade: grade,
            status: status,
            skill: skill,
            account: account,
            email: email,
            phone: phone,
            fb: fb,
            graduation: graduation,
            fulltime: fulltime,
        },
        success: function(data){
            $("#candidate-edit").modal('toggle');
            setTimeout(function(){
                $("#edit-candidate-"+id).click();
            },500)
            /*$("#edit-candidate-message").html("<div class='alert alert-success alert-dismissible'><a href='#' class='close' data-dismiss='alert' aria-label='close'>&times;</a>"+data+"</div>");*/
        },
        error: function(request,status,error){
            console.log(error);
            /*$("#edit-candidate-message").html("<div class='alert alert-danger alert-dismissible'><a href='#' class='close' data-dismiss='alert' aria-label='close'>&times;</a>"+request.responseJSON.message+"</div>");*/
            alert(request.responseJSON.message);
        },
    });
});
$("#candidate-information").on('show.bs.modal',function(){
    $("#edit-candidate-message").html("");
});

$("#candidate-event-list").on('hidden.bs.modal',function(){
    eventsList.clear().draw(false);
});

$("#filter-btn").on('click',function(){
    var params = "";
    var name = $("#name-filter").val();
    var university = $("#university-filter").val();
    var faculty = $("#faculty-filter").val();
    var skill = $("#skill-filter").val();
    var event = $("#event-name-filter").val();
    var fromdate = $("#from-date").val();
    var todate = $("#to-date").val();
    params = name+","+university+","+faculty+","+skill+","+event+","+fromdate+","+todate;
    $.ajax({
        type: "GET",
        url: "/manage/candidates/filter",
        data: {
            params: params,
        },
        success: function(data){
            candidate.clear().draw();
            var res = jQuery.parseJSON(data);
            console.log(res);
            jQuery(res).each(function(i,item){
                var irow = [];
                var b2id = "edit-candidate-"+item.id;
                var b3id = "delete-candidate-"+item.id;
                var button2 = '<button type="button" class="btn btn-success btn-small ml-2 candidate-edit" data-toggle="modal" data-target="#candidate-information" id=\"'+b2id+'\"><i class="fas fa-user-edit"></i></button>';
                var button3 = '<button type="button" class="btn btn-danger btn-small ml-2 candidate-delete" data-toggle="modal" data-target="#candidate-delete" id=\"'+b3id+'\"><i class="fas fa-user-times"></i></button>';
                var cell = button2+button3;
                irow.push(cell);
                irow.push(item.id);
                irow.push(item.name);
                irow.push(item.gender);
                irow.push(item.university.name);
                irow.push(item.faculty.name);
                irow.push(item.email);
                irow.push(item.birthday);
                irow.push(item.skill.name);
                candidate.row.add(irow).draw();
            });

        },
        error: function(e){
            console.log(e);
        },
    });
});
$("#add-confirm").on('click',function(){
    if($("#new-candidate-university").val()!="--SELECT--"){
        var c = confirm("Are you sure you want to add this candidate?");
        var uv = $("#new-candidate-university").val();
        var faculty = $("#new-candidate-faculty").val();
        var course = $("#new-candidate-course").val();
        var name = $("#new-candidate-name").val();
        var gender = "";
        if($("#new-gender-male").is(":checked")){
            gender = "Male";
        }else{
            gender = "Female";
        }
        var email = $("#new-candidate-email").val();
        var account = $("#new-candidate-account").val();
        var phone = $("#new-candidate-phone").val();
        var facebook = $("#new-candidate-facebook").val();
        var status = $("#new-candidate-status").val();
        var fgrade = $("#new-candidate-fgrade").val();
        var clevel = $("#new-candidate-clevel").val();
        var cerid = $("#new-candidate-cerid").val();
        var dob = $("#new-candidate-dob").val();
        var graduation = $("#new-candidate-graduation").val();
        var fulltime = $("#new-candidate-fulltime").val();
        var gpa = $("#new-candidate-gpa").val();
        if(c==true){
            $.ajax({
                type: "GET",
                url: "/manage/candidates/new",
                async: false,
                data: {
                    uv: uv,
                    faculty: faculty,
                    course: course,
                    name: name,
                    gender: gender,
                    email: email,
                    account: account,
                    phone: phone,
                    facebook: facebook,
                    status: status,
                    fgrade: fgrade,
                    clevel: clevel,
                    cerid: cerid,
                    dob: dob,
                    gpa: gpa,
                    graduation: graduation,
                    fulltime: fulltime,
                },
                success: function(data){
                    /*var res = jQuery.parseJSON(data[1]);
                    console.log(res);*/
                    /*$("#new-candidate-message").html("<div class='alert alert-success'>Success add a new candidate</div>");*/

                    /*var b2id = "edit-candidate-"+res.id;
                    var b3id = "delete-candidate-"+res.id;
                    var button2 = '<button type="button" class="btn btn-success btn-small ml-1 candidate-edit" data-toggle="modal" data-target="#candidate-information" id=\"'+b2id+'\"><i class="fas fa-user-edit"></i></button>';
                    var button3 = '<button type="button" class="btn btn-danger btn-small ml-1 candidate-delete" data-toggle="modal" data-target="#candidate-delete" id=\"'+b3id+'\"><i class="fas fa-user-times"></i></button>';
                    var irow = [];
                    irow.push(button2+button3);
                    irow.push(res.id);
                    irow.push(res.name);
                    irow.push(res.gender);
                    irow.push(res.university.name);
                    irow.push(res.faculty.name);
                    irow.push(res.email);
                    irow.push(toDate(res.birthday));
                    irow.push(res.skill.name);
                    candidate.row.add(irow).draw(false);*/
                    $("#new-candidate-modal").modal('toggle');
                    alert(data[0]);
                    window.location.href = "/manage/candidates";

                },
                error: function(request,status,error){
                    /*$("#new-candidate-message").html("<div class='alert alert-danger'>"+request.responseJSON.message+"</div>");*/
                    alert(request.responseJSON.message);
                },
            });
        }
    }
});
$("tbody[id=table-events]").on('click','.changestatus[id^=changestatus]',function(){
    $("#candidate-information").modal('toggle');
    setTimeout(function(){
        $("change-status-modal").modal('show');
    },200);
    var id = $(this).attr('id');
    var candidate = id.split("-")[1];
    var event = id.split("-")[2];
    $("#change-status-modal").modal('toggle');
    $("#change-status-event").text(event);
    $("#change-status-candidate").text(candidate);
    var list = eventsList.row($(this).parents('tr')).data();
    console.log(list);
    $("#change-status").val(list[3]);
    $("#change-grade").val(list[4]);
    $("#change-clevel").val(list[5]);
    $("#change-cerid").val(list[6]);
});
$("#candidate-event-list").on('hidden.bs.modal',function(){
    $("tbody[id=table-events]").empty();
});
$("#close-edit-candidate").on('click',function(){
    var id = $("#candidate-id").text();
    $("#candidate-edit").modal('toggle');
    setTimeout(function(){
        $("#candidate-information").modal('show');
    },500);
})
$("#change-confirm").on('click',function(){
    var candidate = $("#change-status-candidate").text();
    var event = $("#change-status-event").text();
    var status = $("#change-status").val();
    var grade = $("#change-grade").val();
    var clevel = $("#change-clevel").val();
    var cerid = $("#change-cerid").val();
    $.ajax({
        type: "GET",
        data: {
            candidate: candidate,
            event: event,
            status: status,
            grade: grade,
            clevel: clevel,
            cerid: cerid,
        },
        url: "/manage/candidates/changestatus",
        success: function(data){
            console.log(data);
            $("#change-status-modal").modal('toggle');
            var irow = $("#changestatus-"+candidate+"-"+event).parents('tr');
            var list = eventsList.row(irow).data();
            list[3] = status;
            list[4] = grade;
            list[5] = clevel;
            list[6] = cerid;
            eventsList.row(irow).data(list).draw();
        },
        error: function(e){
            console.log(e);
        },
        complete: function(){
            $("#change-status-modal").modal('toggle');
            setTimeout(function(){
                $("#edit-candidate-"+candidate).click();
            },800);
        },
    });
});


$("#candidate-information").on('show.bs.modal',function(){
    getUniversity("c-university","none");
    getFaculty("c-faculty","none");
});
$("#c-university").on('change',function(){
    getUniversityInformation($(this).val(),"#c-faculty");
});
$("#candidate-information").on('hidden.bs.modal',function(){
});
$("#change-status-modal").on('hidden.bs.modal',function(){
    $("#change-status-candidate").text("");
    $("#change-status-event").text("");
    $("#change-grade").val("");
    $("#change-clevel").val("");
    $("#change-cerid").val("");
});
$("#change-cancel").on('click',function(){
    $("#change-status-modal").modal('toggle');
    var candidate = $("#change-status-candidate").text();
    setTimeout(function(){
        $("#edit-candidate-"+candidate).click();
    },500);
    $("#candidate-information").scrollTop(500);
});
$("tbody[id=table-events]").on('click','.remove[id^=remove]',function(){
    var c = confirm("You sure you want to remove this event from candidate's event list?");
    if(c==true){
        var id = $(this).attr('id');
        var candidate = id.split("-")[1];
        var event = id.split("-")[2];
        $.ajax({
            type: "GET",
            data: {
                candidate: candidate,
                event: event,
            },
            url: "/manage/candidates/remove",
            success: function(data){
                if(data == "Success"){
                    var irow = $("#remove-"+candidate+"-"+event).parents('tr');
                    eventsList.row(irow).remove().draw(false);
                    console.log("Remove Success");
                }

            },
            error: function(e){
                console.log(e);
            }
        });
    }
});
$("#new-candidate-university").on('change',function(){
    getUniversityInformation($(this).val(),"#new-candidate-faculty");
    getEventsByUniversityAndFaculty($(this).val(),$("#new-candidate-faculty").val(),"#new-candidate-course");
});

$("#new-candidate-faculty").on('change',function(){
    getEventsByUniversityAndFaculty($("#new-candidate-university").val(),$(this).val(),"#new-candidate-course");
});
$("#new-candidate-email").on('keyup',function(){
    $("#new-candidate-account").val($(this).val());
})
$("#new-candidate-btn").on('hidden.bs.modal',function(){
    $("#new-candidate-university").empty();
    $("#new-candidate-faculty").empty();
    $("#new-candidate-course").empty();
});
$(document).ready(function(){
    getUniversity("university-filter","ALL");
    getUniversity("new-candidate-university","SELECT");
    getUniversity("filter-university","none");
    getSkill("filter-skill","none");
    getEventNames("filter-event","none");
    loadError(errorTable);
});

//********//
function getEvents(id,table){
    table.clear().draw();
    $.ajax({
        type: "GET",
        url: "/manage/candidates/getEvents",
        data: {
            id: id,
        },
        success: function(data){
            var res = jQuery.parseJSON(data);
            console.log(res);
            jQuery(res).each(function(i,item){
                var id1 = "remove-"+item.candidate.id+"-"+item.event.id;
                var id2 = "changestatus-"+item.candidate.id+"-"+item.event.id;
                var btn1 = '<button class="btn btn-danger btn-small ml-2 remove" id=\"'+id1+'\"><i class="fas fa-minus-circle"></i></button>';
                var btn2 = '<button class="btn btn-success btn-small ml-2 changestatus" id=\"'+id2+'\"><i class="fas fa-pencil-alt"></i></button>';
                irow = [];
                irow.push(btn1+btn2);
                irow.push(item.event.code);
                irow.push(item.event.name);
                irow.push(item.status);
                irow.push(item.finalGrade);
                irow.push(item.completionLevel);
                irow.push(item.certificateId);
                table.row.add(irow).draw();
            })

        },
        error: function(e){
            console.log(e);
        }
    });
}
function toDate(sDate){
    if(sDate==null||sDate=="null"){
        return "-";
    }else{
        var date = new Date(sDate);
        var d = date.getDate();
        var m = date.getMonth()+1;
        var y = date.getFullYear();
        if(d<10){
            d = "0"+d;
        }
        if(m<10){
            m = "0"+m;
        }
        return y+"/"+m+"/"+d;
    }
};

/***/
$("#university-filter").on('change',function(){
    if($(this).val()=="--ALL--"){
        $("#faculty-filter").empty();
        $("#skill-filter").empty();
        $("#event-name-filter").empty();
    }else{
        getUniversityInformation($(this).val(),"#faculty-filter");
        getEventsByUniversityAndFaculty($(this).val(),$("#faculty-filter").val(),"#event-name-filter");
    }
});
$("#faculty-filter").on('change',function(){
    getEventsByUniversityAndFaculty($("#university-filter").val(),$(this).val(),"#event-name-filter");
});
$("#skill-filter").on('change',function(){
    var university = $("#university-filter").val();
    var faculty = $("#faculty-filter").val();
    var skill = $(this).val();
    $.ajax({
        type: "GET",
        data: {
            university: university,
            faculty: faculty,
            skill: skill,
        },
        url: "/manage/getEventDetail",
        success: function(data){
            console.log(data);
            $("#event-name-filter").empty();
            $("#event-name-filter").append("<option>--ALL--</option>");
            jQuery(data).each(function(i,item){
                 $("#event-name-filter").append("<option>"+item+"</option>");
            });
        },
        error: function(e){
            console.log(e);
        }
    });
});
$("#event-name-filter").on('change',function(){
    if($(this).val()!="--ALL--"){
        var code = $(this).val();
            $.ajax({
                type: "GET",
                data: {
                    code: code,
                },
                url: "/manage/getFilterSkill",
                success: function(data){
                    $("#skill-filter").empty();
                    $("#skill-filter").append("<option>"+data+"</option>");
                },
                error: function(e){
                    console.log(e);
                }
            });
    }

});


/****/
function getSkillByUandF(university,faculty){
    $.ajax({
        type: "GET",
        data: {
            university: university,
            faculty: faculty,
        },
        async: false,
        url: "/manage/getSkillByUaF",
        success: function(data){
            console.log(data);
            $("#skill-filter").empty();
            jQuery(data).each(function(i,item){
                $("#skill-filter").append("<option>"+item+"</option>");
            });
        },
        error: function(e){
            console.log(e);
        }
    });
};
function getUniversityInformation(university,selectid){
    $.ajax({
        type: "GET",
        data: {
            university: university,
        },
        async: false,
        url: "/manage/getUniversityFaculty",
        success: function(data){
            console.log(data);
            $(selectid).empty();
            jQuery(data).each(function(i,item){
                $(selectid).append("<option>"+item+"</option>");
            });
        },
        error: function(e){
            console.log(e);
        }
    });
};

function getUniversityByFaculty(faculty){
    $.ajax({
        type: "GET",
        data: {
            faculty: faculty,
        },
        async: false,
        url: "/manage/getFacultyUniversity",
        success: function(data){
            console.log(data);
            $("#university-filter").empty();
            jQuery(data).each(function(i,item){
                $("#university-filter").append("<option>"+item+"</option>");
            });
            var university = $("#university-filter").val();
            var f = $("#faculty-filter").val();
            getEventsByUniversityAndFaculty(university,f);

        },
        error: function(e){
            console.log(e);
        }
    });
};
function getEventsByUniversityAndFaculty(university,faculty,selectid){
    if(selectid=="none"){
    }else{
        $.ajax({
            type: "GET",
            data: {
                university: university,
                faculty: faculty,
            },
            async: false,
            url: "/manage/getEventByUniversityAndFaculty",
            success: function(data){
                console.log(data);
                $(selectid).empty();
                jQuery(data).each(function(i,item){
                    $(selectid).append("<option>"+item+"</option>");
                });
                /*getSkillByUandF(university,faculty);*/
            },
            error: function(e){
                console.log(e);
            }
        });
    }
};
function getUniversity(optionid,firstOption){
    $.ajax({
        type: "GET",
        url: "/manage/getUniversity",
        async: false,
        success: function(data){
            if(firstOption!="none"){
                $("#"+optionid).append('<option>--'+firstOption+'--</option>');
            }
            for(i=0;i<data.length;i++){
                $("#"+optionid).append('<option>'+data[i]+'</option>');
            }
        },
        error: function(e){
            console.log(e);
        },
    })
};
function getFaculty(optionid,firstOption){
    $.ajax({
        type: "GET",
        url: "/manage/getFaculty",
        async: false,
        success: function(data){
            $("#"+optionid).empty();
            if(firstOption!="none"){
                $("#"+optionid).append('<option>--'+firstOption+'--</option>');
            }
            for(i=0;i<data.length;i++){
                $("#"+optionid).append('<option>'+data[i]+'</option>');
            }
        },
        error: function(e){
            console.log(e);
        },
    });
};

function getSkill(optionid,firstOption){
    $.ajax({
        type: "GET",
        url: "/manage/getSkill",
        async: false,
        success: function(data){
            $("#"+optionid).empty();
            if(firstOption!="none"){
                $("#"+optionid).append('<option>--'+firstOption+'--</option>');
            }
            for(i=0;i<data.length;i++){
                $("#"+optionid).append('<option>'+data[i]+'</option>');
            }
        },
        error: function(e){
            console.log(e);
        },
    })
};
function getEventNames(optionid,firstOption){
    $.ajax({
        type: "GET",
        url: "/manage/getEventNames",
        async: false,
        success: function(data){
            $("#"+optionid).empty();
            if(firstOption!="none"){
                $("#"+optionid).append('<option>--'+firstOption+'--</option>');
            }
            for(i=0;i<data.length;i++){
                var j=i+2;
                $("#"+optionid).append('<option>'+data[i]+'</option>');
            }
        },
        error: function(e){
            console.log(e);
        },
    })
};
