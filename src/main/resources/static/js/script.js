

  $(document).ready(function() {
    var events = [];
    $('#calendar').fullCalendar({
      defaultDate: Date.now(),
      editable: false,
      eventLimit: true, // allow "more" link when too many events
      events: events,
      eventTextColor: "white",
    });

         $.ajax({
            type: "GET",
            url: "/manage/candidates/getEventDate",
            success: function(data){
                jQuery(data).each(function(i,item){
                    var e =  {title: item[2], start: item[0], end: item[1],color: generateColor()};//date: item[0], title: convert(item[0])+" to "+convert(item[1]), location: item[2]};
                    events.push(e);
                });

                $('#calendar').fullCalendar( 'addEventSource', events );

            },
            error: function(e){
                console.log(e);
            },
         });


    function convert(str){
        var d = str.split("-")[2];
        var m = str.split("-")[1];
        var y = str.split("-")[0];
        return d+"/"+m+"/"+y;
    }
  });