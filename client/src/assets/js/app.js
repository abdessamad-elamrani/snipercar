$(document).ready(function () {


  $(document).delegate('#menu_toggle', 'click', function () {
    toggleSidebar();
  });
  $(document).delegate('.close_menuMobile,.side-menu .child_menu .panel-collapse a,.navbar-nav .open .dropdown-menu>li', 'click', function () {

    toggleSidebar();
  });
  $(document).delegate('.back_menuMobile, .side-menu .child_menu .panel-collapse a', 'click', function () {
    $('.child_menu').css({ 'opacity': '0', 'visibility': 'hidden', 'animation': 'slideRight 0.3s' });
  });

  $(document).delegate('.nav.side-menu>li>a', 'click', function () {
    if (!$(this).hasClass('has-child')) {
      toggleSidebar();
    }
  });
  $(document).delegate('.top_nav .fixed_mobile li', 'click', function () {
    var attr = $(this).attr('data-toggle');
    if (typeof attr !== typeof undefined && attr !== false) {
      toggleSidebar();
    }
  });

  function toggleSidebar() {
    const $menuFixed = $('.menu_fixed');
    const $fixedMobile = $('.fixed_mobile');
    const $navMenu = $('.nav_menu');
    if ($menuFixed.hasClass('open')) {
      $menuFixed.removeClass('open');
      $fixedMobile.removeClass('open');
      $navMenu.removeClass('bg_mobile');
      $('.child_menu').css({ 'opacity': '0', 'visibility': 'hidden', 'animation': 'slideRight 0.3s' });
    } else {
      $menuFixed.addClass('open');
      $fixedMobile.addClass('open');
      $navMenu.addClass('bg_mobile');
    }
  }


  $(document).delegate('.collapse-filter[data-toggle="collapse"]', 'click', function () {
    $(this).children('i').toggleClass('fa-chevron-up fa-chevron-down')
  });


  $('.inputfile').each(function () {
    var $input = $(this),
      $label = $input.next('label'),
      labelVal = $label.html();

    $input.on('change', function (e) {
      var fileName = '';

      if (this.files && this.files.length > 1)
        fileName = (this.getAttribute('data-multiple-caption') || '').replace('{count}', this.files.length);
      else if (e.target.value)
        fileName = e.target.value.split('\\').pop();

      if (fileName)
        $label.find('.archive-name').html(fileName);
      else
        $label.html(labelVal);
    });
    $input
      .on('focus', function () {
        $input.addClass('has-focus');
      })
      .on('blur', function () {
        $input.removeClass('has-focus');
      });
  });

});