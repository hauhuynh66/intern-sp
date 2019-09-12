var counter = 0;
$("#profile-enable-edit").on('click',function(){
    if(counter%2==0){
        $(this).html('<i class="fas fa-lock green-icon nav-icon"></i>');
        $("#profile-name").prop('disabled',false);
        $("#profile-phone").prop('disabled',false);
        $("#profile-unit").prop('disabled',false);
        $("#profile-email").prop('disabled',false);
    }else{
        $(this).html('<i class="fas fa-lock-open green-icon nav-icon"></i>');
        $("#profile-name").prop('disabled',true);
        $("#profile-phone").prop('disabled',true);
        $("#profile-unit").prop('disabled',true);
        $("#profile-email").prop('disabled',true);
    }
    counter++;
});
$("#logout-confirm").on('click',function(){
    window.location.href = "http://localhost:8400/logout";
});

(function($) {
  "use strict"; // Start of use strict

  // Toggle the side navigation
  $("#sidebarToggle, #sidebarToggleTop").on('click', function(e) {
    $("body").toggleClass("sidebar-toggled");
    $(".sidebar").toggleClass("toggled");
    if ($(".sidebar").hasClass("toggled")) {
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Close any open menu accordions when window is resized below 768px
  $(window).resize(function() {
    if ($(window).width() < 768) {
      $('.sidebar .collapse').collapse('hide');
    };
  });

  // Prevent the content wrapper from scrolling when the fixed side navigation hovered over
  $('body.fixed-nav .sidebar').on('mousewheel DOMMouseScroll wheel', function(e) {
    if ($(window).width() > 768) {
      var e0 = e.originalEvent,
        delta = e0.wheelDelta || -e0.detail;
      this.scrollTop += (delta < 0 ? 1 : -1) * 30;
      e.preventDefault();
    }
  });

  // Scroll to top button appear
  $(document).on('scroll', function() {
    var scrollDistance = $(this).scrollTop();
    if (scrollDistance > 100) {
      $('.scroll-to-top').fadeIn();
    } else {
      $('.scroll-to-top').fadeOut();
    }
  });

  // Smooth scrolling using jQuery easing
  $(document).on('click', 'a.scroll-to-top', function(e) {
    var $anchor = $(this);
    $('html, body').stop().animate({
      scrollTop: ($($anchor.attr('href')).offset().top)
    }, 1000, 'easeInOutExpo');
    e.preventDefault();
  });

})(jQuery); // End of use strict
function loadError(table){
    $.ajax({
            type: "GET",
            url: "/manage/getErrors",
            success: function(data){
                var res = data;
                jQuery(res).each(function (i,item){
                    var irow = [];
                    irow.push(item.row);
                    irow.push(item.content);
                    table.row.add(irow).draw(false);
                });
                console.log(data);

            },
            error: function(e){
                console.log(e);
            }
        });
}
/*

function showAlert(modal,id,message){
    $(modal).modal('toggle');
    $(id).text(message);
    setTimeout(function(){
        $(modal).modal('hide');
    },3000);
};*/
