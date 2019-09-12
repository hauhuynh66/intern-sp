$("#planned-to-date").datepicker({autoclose: true});
$("#planned-from-date").datepicker({autoclose: true});
$("#actual-to-date").datepicker({autoclose: true});
$("#actual-from-date").datepicker({autoclose: true});

$("#e-actual-start-date").datepicker({autoclose: true});
$("#e-actual-end-date").datepicker({autoclose: true});
$("#e-end-date").datepicker({autoclose: true});
$("#e-start-date").datepicker({autoclose: true});

var eventTable = $('#event-table').DataTable({"scrollY": 600,"scrollX": true,"order":[[8,"asc"]],});
var errorTable = $("#upload-errors").DataTable({"scrollX": true,"order":[[0,"asc"]],});
var candidateList = $("#candidate-list").DataTable({"lengthMenu":[[20,40,100,250],[20,40,100,250]],"scrollY": 1000,"scrollX": true,"order":[[1,"asc"]]});
$("tbody[id=event-body]").on('click','.deletebtn[id^=deletebtn]',function(){
   var str = $(this).attr('id');
   var id = str.split("-")[1];
   $("#delete-id").text(id);
});

$("#import-btn").click(function(event){
    event.preventDefault();
    submitForm();
});
function submitForm(){
    var form = $("#import-form")[0];
    var data = new FormData(form);
    if(data==null){
        alert("Please choose a file");
    }else{
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
                    },
                    error: function(jqXHR, textStatus, errorThrown) {
                        message = "<div class='alert alert-danger'>Error</div>"
                        $("#result").html(message);
                        console.log("ERROR : ", jqXHR.responseText);
                        $("#import-form").prop("disabled", false);
                    },
                    complete: function(){
                        window.location.href = "/manage/events";
                    }
                });
    }
};
$("#export-btn").on('click',function(){
    var fileName = $("#export-filename").val()+".xlsx";
    var target = $("#export-message");
    var list = eventTable.column(1).data().toArray();
    var eventList = "";
    jQuery(list).each(function(i,item){
        eventList += item+"+";
    })
    var message = "";
    $.ajax({
        type: "GET",
        url: "/manage/events/export",
        data: {
            fileName : fileName,
            event : eventList,
        },
        success: function(data){
            window.location.href = "/manage/download?fileName="+fileName;
        },
        error: function(e){
            console.log(e);
        },
    });
});
$("#delete-event-confirm").on('click',function(){
    var id = $("#delete-id").text();
    $.ajax({
        type: "GET",
        url: "http://localhost:8400/manage/events/delete?id="+id,
        success: function(data){
            console.log(data);
            var irow = $("#event-row-"+id);
            eventTable.row(irow).remove().draw();
            $("#event-delete").modal('toggle');
        },
        error: function(request,status,error){
            $("#delete-event-message").text(request.responseJSON.message);
            setTimeout(function(){
                $("#delete-event-message").empty();
            },5000);
            console.log(error);
        }
    });
});
$("tbody[id=event-body]").on('click','.candidate-list[id^=candidate-list-]',function(){
    var s = $(this).attr('id');
    var id = s.split("-")[2];
    var code = $("#courseCode-"+id).text();
    $.ajax({
        type: "GET",
        url: "http://localhost:8400/manage/events/getCandidates?code="+code,
        success: function(data){
            $("#course-code").text(code);
            data = jQuery.parseJSON(data);
            console.log(data);
            candidateList.clear().draw(false);
            jQuery(data).each(function(i,item){
                var list = [];
                var id = "delc-"+item.candidate.id;
                var button = '<button class="btn btn-danger btn-small delc" id=\"'+id+'\"><i class="fas fa-minus-circle"></i></button>';
                list.push(button);
                list.push(item.candidate.id);
                list.push(item.candidate.name);
                list.push(item.candidate.gender);
                list.push(toDate(item.candidate.birthDay));
                list.push(item.candidate.email);
                list.push(item.candidate.university);
                list.push(item.candidate.faculty);
                list.push(item.status);
                candidateList.row.add(list).draw(false);
            });
        },
        error: function(e){
            console.log(e);
        }
    });
});
$("tbody[id=table-candidate]").on('click','.delc[id^=delc]',function(){
    var s = $(this).attr('id');
    var id = s.split("-")[1];
    var code = $("#course-code").text();
    var str = "Are you sure you want to remove this candidate from this list?";
    var c = confirm(str);
    if(c==true){
        $.ajax({
            type: "GET",
            url: "/manage/events/removeCandidate",
            data: {
                code: code,
                id: id,
            },
            success: function(data){
                irow = $("#delc-"+id).parents('tr');
                candidateList.row(irow).remove().draw(false);
                console.log(data);
            },
            error: function(e){
                console.log(e);
            },
        });
    }else{
        alert("OK");
    }

});
$("tbody[id=event-body]").on('click','.editbtn[id^=editbtn]',function(){
    var s = $(this).attr('id');
    var id = s.split("-")[1];
    var code = $("#courseCode-"+id).text();
    $.ajax({
        type: "GET",
        url: "/manage/events/get",
        data: {
            code: code,
        },
        success: function(data){
            $("#event-code").text(code);
            var res = jQuery.parseJSON(data[2]);
            console.log(res);
            $("#e-site").val(res.site.name);
            $("#e-name").val(res.program.name);
            $("#e-subject").val(res.subjectType);
            $("#e-sub-subject").val(res.skill.name);
            $("#e-format").val(res.format);
            $("#e-partner").val(res.university.name);
            $("#e-start-date").val(res.plannedStartDate);
            $("#e-end-date").val(res.planEndDate);
            $("#e-start-date").val(res.planStartDate);
            $("#e-n-hours").text(res.program.time);
            $("#e-actual-start-date").val(res.actualStartDate);
            $("#e-actual-end-date").val(res.actualEndDate);
            $("#e-n-actual-hours").text(res.actualLearningTime);
            $("#e-n-students").text(data[0]);
            $("#e-n-actual-students").text(data[1]);
            $("#event-site").text(res.site.name);
            $("#event-name").text(res.program.name);
            $("#event-sub-subject").text(res.skill.name);
            $("#event-n-students").text(data[0]);
            $("#event-partner").text(res.university.name);
            $("#event-subject").text(res.subjectType);
            $("#event-format").text(res.format);
            $("#event-plan-startDate").text(res.planStartDate);
            $("#event-plan-endDate").text(res.planEndDate);
            $("#event-plan-students").text(data[0]);
            $("#event-budgetCode").text(res.budgetCode);
            $("#event-n-hours").text(res.program.time);
            $("#event-actual-startDate").text(res.actualStartDate);
            $("#event-actual-endDate").text(res.actualEndDate);
            $("#event-actual-students").text(data[1]);
            $("#event-actual-n-hours").text(res.actualLearningTime);
            $("#event-id").text(id);
        },
        error: function(e){
            console.log(e);
        },
    });
});
$("#event-editbtn").on('click',function(){
    $("#event-information").modal('toggle');
    setTimeout(function(){
        $("#event-edit").modal("show");
    },500);
});
$("#edit-event-dismiss").on('click',function(){
    $("#event-edit").modal('toggle');
    setTimeout(function(){
        $("#editbtn-"+$("#event-id").text()).click();
    },500);
});
$(".modal").on('shown.bs.modal',function(){
    $(this).focus();
});
$(document).ready(function(){
    getSkill("filter-skill","none");
    getUniversity("filter-university","none");
    getProgram("filter-program","none");
    loadError(errorTable);
});
$("#filter").on('click',function(){
    var sites = $("#filter-site").val();
    var skills = $("#filter-skill").val();
    var statuses = $("#filter-status").val();
    var universities = $("#filter-university").val();
    var programs = $("#filter-program").val();
    $.ajax({
        type: "GET",
        url: "/manage/events/filter",
        data : {
            sites: sites,
            skills: skills,
            statuses: statuses,
            universities: universities,
            programs: programs,
        },
        success: function(data){
            var res = jQuery.parseJSON(data);
            console.log(res);
            eventTable.clear().draw(false);
            jQuery(res).each(function(i,item){
                var irow = [];
                var b1id = "candidate-list-"+item.id;
                var b2id = "editbtn"+item.id;
                var b3id = "deletebtn"+item.id;
                var button1 = '<button type="button" class="btn btn-info btn-small ml-2 candidate-list" data-toggle="modal" data-target="#candidate-attend-list" id=\"'+b1id+'\"><i class="fas fa-search"></i></button>';
                var button2 = '<button type="button" class="btn btn-success btn-small ml-2 editbtn" data-toggle="modal" data-target="#event-information" id=\"'+b2id+'\"><i class="fas fa-edit"></i></button>';
                var button3 = '<button type="button" class="btn btn-danger btn-small ml-2 deletebtn" data-toggle="modal" data-target="#event-delete" id=\"'+b3id+'\"><i class="fas fa-calendar-times"></i></button>';
                var cell = button1+button2+button3;
                irow.push(cell);
                irow.push(item.courseCode);
                irow.push(item.program.name);
                irow.push(item.skill.name);
                irow.push(item.students);
                irow.push(item.university.name);
                irow.push(toDate(item.planStartDate));
                irow.push(toDate(item.planEndDate));
                irow.push(toDate(item.actualStartDate));
                irow.push(toDate(item.actualEndDate));
                eventTable.row.add(irow).draw();
            });
        },
        error: function(e){
            console.log(e);
        },
    });
});
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
function getProgram(optionid,firstOption){
    $.ajax({
        type: "GET",
        url: "/manage/getPrograms",
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