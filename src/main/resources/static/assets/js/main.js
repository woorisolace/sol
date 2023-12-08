(function ($) {
    "use strict";
    
    
    
    /*------ SVG img active ----*/
    SVGInject($('.injectable'));
    
    
    /*--------------------------------
        Slider active 1
    -----------------------------------*/
    $('.slider-active-1').on('init', function(event){
        SVGInject($(event.target).find('.injectable'));
    }).slick({
        slidesToShow: 1,
        slidesToScroll: 1,
        fade: true,
        loop: true,
        dots: false,
        arrows: true,
        prevArrow: '<span class="slider-icon-prev"><img class="injectable" src="assets/images/icon-img/arrow-left.svg" alt=""></span>',
        nextArrow: '<span class="slider-icon-next"><img class="injectable" src="assets/images/icon-img/arrow-right.svg" alt=""></span>',
        responsive: [{
                breakpoint: 1500,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 1199,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 1,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 767,
                settings: {
                    autoplay: false,
                    slidesToShow: 1,
                    slidesToScroll: 1,
                }
            }
        ]
    });
    
    
    /*------ Brand logo active 1 ----*/
    $('.brand-logo-active').slick({
        slidesToShow: 5,
        slidesToScroll: 1,
        fade: false,
        loop: true,
        dots: false,
        arrows: false,
        responsive: [
            {
                breakpoint: 1199,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 575,
                settings: {
                    slidesToShow: 2,
                }
            }
        ]
    });
    
    /*------ Wow Active ----*/
    new WOW().init();
    
    
    /*--------------------------------
        New book slider
    -----------------------------------*/
    $('.product-slider-active').on('init', function(event){
        SVGInject($(event.target).find('.injectable'));
    }).slick({
        slidesToShow: 2,
        slidesToScroll: 1,
        loop: true,
        dots: false,
        arrows: true,
        prevArrow: '<span class="product-icon-prev"><img class="injectable" src="assets/images/icon-img/arrow-left-2.svg" alt=""></span>',
        nextArrow: '<span class="product-icon-next"><img class="injectable" src="assets/images/icon-img/arrow-right-2.svg" alt=""></span>',
        responsive: [{
                breakpoint: 1500,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 1199,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 2,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 767,
                settings: {
                    autoplay: false,
                    slidesToShow: 2,
                    slidesToScroll: 1,
                }
            },
            {
                breakpoint: 575,
                settings: {
                    autoplay: false,
                    slidesToShow: 1,
                    slidesToScroll: 1,
                }
            }
        ]
    });
    
    
    /*--------------------------------
        Blog image slider
    -----------------------------------*/
    $('.blog-img-slider').on('init', function(event){
        SVGInject($(event.target).find('.injectable'));
    }).slick({
        slidesToShow: 1,
        slidesToScroll: 1,
        loop: true,
        dots: false,
        arrows: true,
        prevArrow: '<span class="product-icon-prev-2"><img class="injectable" src="assets/images/icon-img/arrow-left-3.svg" alt=""></span>',
        nextArrow: '<span class="product-icon-next-2"><img class="injectable" src="assets/images/icon-img/arrow-right-3.svg" alt=""></span>',
    }); 
    
    
    
    /*--------------------------------
        New book slider
    -----------------------------------*/
    $('.product-slider-active-2').slick({
        slidesToShow: 4,
        slidesToScroll: 1,
        fade: false,
        loop: true,
        dots: false,
        arrows: false,
        responsive: [
            {
                breakpoint: 1199,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 575,
                settings: {
                    slidesToShow: 1,
                }
            }
        ]
    });
    
    
    /*--------------------------------
        New book slider
    -----------------------------------*/
    $('.instagram-slider-active').slick({
        slidesToShow: 4,
        slidesToScroll: 1,
        fade: false,
        loop: true,
        dots: false,
        arrows: false,
        responsive: [
            {
                breakpoint: 1199,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 4,
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 575,
                settings: {
                    slidesToShow: 2,
                }
            }
        ]
    });
    
    
    
    /*====== SidebarCart ======*/
    function miniCart() {
        var navbarTrigger = $('.cart-active'),
            endTrigger = $('.cart-close'),
            container = $('.sidebar-cart-active'),
            wrapper = $('.main-wrapper');
        
        wrapper.prepend('<div class="body-overlay"></div>');
        
        navbarTrigger.on('click', function(e) {
            e.preventDefault();
            container.addClass('inside');
            wrapper.addClass('overlay-active');
        });
        
        endTrigger.on('click', function() {
            container.removeClass('inside');
            wrapper.removeClass('overlay-active');
        });
        
        $('.body-overlay').on('click', function() {
            container.removeClass('inside');
            wrapper.removeClass('overlay-active');
        });
    };
    miniCart();
    
    /*====== Menu active ======*/
    function mainMenu() {
        var navbarTrigger = $('.menu-active-button'),
            endTrigger = $('.off-canvas-close'),
            container = $('.off-canvas-active'),
            wrapper = $('.wrapper-2');
        
        wrapper.prepend('<div class="body-overlay-2"></div>');
        
        navbarTrigger.on('click', function(e) {
            e.preventDefault();
            container.addClass('inside');
            wrapper.addClass('overlay-active-2');
        });
        
        endTrigger.on('click', function() {
            container.removeClass('inside');
            wrapper.removeClass('overlay-active-2');
        });
        
        $('.body-overlay-2').on('click', function() {
            container.removeClass('inside');
            wrapper.removeClass('overlay-active-2');
        });
    };
    mainMenu();
    
    const slinky = $('#menu').slinky();
    
    
    
    
    /*---------------------
        Price range
    --------------------- */
    var sliderrange = $('#slider-range');
    var amountprice = $('#amount');
    $(function() {
        sliderrange.slider({
            range: true,
            min: 16,
            max: 400,
            values: [0, 300],
            slide: function(event, ui) {
                amountprice.val("$" + ui.values[0] + " - $" + ui.values[1]);
            }
        });
        amountprice.val("$" + sliderrange.slider("values", 0) +
            " - $" + sliderrange.slider("values", 1));
    }); 
    
    
    /*-----------------------
        Shop filter active 
    ------------------------- */
    $('.shop-filter-active').on('click', function(e) {
        e.preventDefault();
        $('.product-filter-wrapper').slideToggle();
    })
    var shopFiltericon = $('.shop-filter-active');
    shopFiltericon.on('click', function() {
        $('.shop-filter-active').toggleClass('active');
    })
    
    
    /*---------------------
        toggle item active
    --------------------- */
    function itemToggler2() {
        $(".toggle-item-active").slice(0, 16).show();
        $(".item-wrapper").find(".loadMore").on('click', function(e) {
            e.preventDefault();
            $(this).parents(".item-wrapper").find(".toggle-item-active:hidden").slice(0, 4).slideDown();
            if ($(".toggle-item-active:hidden").length == 0) {
                $(this).parent('.toggle-btn').fadeOut('slow');
            }
        });
    }
    itemToggler2();
    
    /* -------------------
        Countdown
    * ---------------------*/ 
    $('.timer-1 [data-countdown]').each(function() {
        var $this = $(this),
            finalDate = $(this).data('countdown');
        $this.countdown(finalDate, function(event) {
            $this.html(event.strftime('<span class="cdown day"> <span>%-D </span><p>Days</p></span> <span class="cdown hour"> <span> %-H</span> <p>Hours</p></span> <span class="cdown minutes"><span>%M</span> <p>Mins</p> </span> <span class="cdown second"><span>%S</span> <p>Secs</p> </span>'));
        });
    });
    
    
    
    /*----------------------------
     sticky active
    ------------------------------ */  
    
	const stickyBar = $('.sticky-bar');
	if(stickyBar.length > 0){
		var stickyTop = stickyBar.offset().top;
		var $window = $(window);
		$window.on('scroll', function () {
			if ($window.scrollTop() > stickyTop) {
				$('.sticky-bar').addClass('stick');
			} else {
				$('.sticky-bar').removeClass('stick');
			}
		});
	}

    
    
    /*---------------------
        Video popup
    --------------------- */
    $('.video-popup-active').magnificPopup({
        type: 'iframe',
        mainClass: 'mfp-fade',
        removalDelay: 160,
        preloader: false,
        zoom: {
            enabled: true,
        }
    });
    
    
    
    /*----------------------------
    	Cart Plus Minus Button
    ------------------------------ */
    var CartPlusMinus = $('.product-quality');
    CartPlusMinus.prepend('<div class="dec qtybutton">-</div>');
    CartPlusMinus.append('<div class="inc qtybutton">+</div>');
    $(".qtybutton").on("click", function() {
        var $button = $(this);
        var oldValue = $button.parent().find("input").val();
        if ($button.text() === "+") {
            var newVal = parseFloat(oldValue) + 1;
        } else {
            // Don't allow decrementing below zero
            if (oldValue > 1) {
                var newVal = parseFloat(oldValue) - 1;
            } else {
                newVal = 1;
            }
        }
        $button.parent().find("input").val(newVal);
    });
    
    /*--------------------------------
        New book slider
    -----------------------------------*/
    $('.related-product-active').slick({
        slidesToShow: 4,
        slidesToScroll: 1,
        fade: false,
        loop: true,
        dots: false,
        arrows: false,
        responsive: [
            {
                breakpoint: 991,
                settings: {
                    slidesToShow: 3,
                }
            },
            {
                breakpoint: 767,
                settings: {
                    slidesToShow: 2,
                }
            },
            {
                breakpoint: 575,
                settings: {
                    slidesToShow: 1,
                }
            }
        ]
    });
    
    /*-------------------------------------
        Product details big image slider
    ---------------------------------------*/
    $('.pro-dec-big-img-slider').slick({
        slidesToShow: 1,
        slidesToScroll: 1,
        arrows: false,
        draggable: false,
        fade: false,
        asNavFor: '.product-dec-slider-small , .product-dec-slider-small-2',
    });
    
    /*---------------------------------------
        Product details small image slider
    -----------------------------------------*/
    $('.product-dec-slider-small').slick({
        slidesToShow: 3,
        slidesToScroll: 1,
        asNavFor: '.pro-dec-big-img-slider',
        dots: false,
        focusOnSelect: true,
        fade: false,
        prevArrow: '<span class="pro-dec-prev"><img class="injectable" src="assets/images/icon-img/arrow-left-5.svg" alt=""></span>',
        nextArrow: '<span class="pro-dec-next"><img class="injectable" src="assets/images/icon-img/arrow-right-5.svg" alt=""></span>',
        responsive: [{
                breakpoint: 767,
                settings: {
                    
                }
            },
            {
                breakpoint: 575,
                settings: {
                    autoplay: true,
                    slidesToShow: 4,
                }
            }
        ]
    });
    
    // Instantiate EasyZoom instances
    var $easyzoom = $('.easyzoom').easyZoom();
    
    /*------ ScrollUp -------- */
    $.scrollUp({
        scrollText: '<i class="las la-angle-up"></i>',
        easingType: 'linear',
        scrollSpeed: 900,
        animation: 'fade'
    });
    
    /*---------------------
        Select active
    --------------------- */
    $('.select-active').select2();
    $(window).on('resize', function(){
        $('.select-active').select2()
    });
    
    
    /*--- checkout toggle function ----*/
    $('.checkout-click1').on('click', function(e) {
        e.preventDefault();
        $('.checkout-login-info').slideToggle(900);
    });
    
    
    /*--- checkout toggle function ----*/
    $('.checkout-click3').on('click', function(e) {
        e.preventDefault();
        $('.checkout-login-info3').slideToggle(1000);
    });
    
    /*-------------------------
    Create an account toggle
    --------------------------*/
    $('.checkout-toggle2').on('click', function() {
        $('.open-toggle2').slideToggle(1000);
    });
    
    $('.checkout-toggle').on('click', function() {
        $('.open-toggle').slideToggle(1000);
    });
    
    /*-------------------------
    checkout one click toggle function
    --------------------------*/
    var checked = $( '.sin-payment input:checked' )
    if(checked){
        $(checked).siblings( '.payment-box' ).slideDown(900);
    };
	 $( '.sin-payment input' ).on('change', function() {
        $( '.payment-box' ).slideUp(900);
        $(this).siblings( '.payment-box' ).slideToggle(900);
    });
    
    /*-------------------------
      Scroll Animation
    --------------------------*/
    AOS.init({
        once: true,
        duration: 1000,
    });
    
    /* NiceSelect */
    $('.nice-select').niceSelect();
    
    
    
    
})(jQuery);

