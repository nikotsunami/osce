
			$(document).ready(function() {
				var $content = $('#content');
				var $htmlBody = $("html, body");
				var isAnimating = false;
				
				/* append backlinks to top to headers*/
				$content.find("h1, h2, h3, h4, h5, h6").each(function() {
					var $h = $(this);
					if (!$h.hasClass('title')) {
						$h.append('<a title="jump back to top" href="#" class="toplink">^</a>');
					}
				});
				
				$content.find("blockquote").children("p").children("strong").each(function() {
					var $s = $(this);
					$s.css({'font-size': '1.2em'});
					var idString = $s.html();
					if (idString.search("Example") >= 0) {
						idString = idString.replace("Example: ", "example").toLowerCase();
						idString = idString.replace(/ /g, "-");
						console.log(idString);
						$s.attr("id", idString);
					}
				});
				
				/* Add anchor scroll animation */
				$("a[href^=#]").click(function(event) {
					var targetOffset;
					event.preventDefault();
					
					if (this.hash.length > 0) {
						console.log($(this.hash));
						var $hash = $(this.hash);
						if ($hash.offset()) {
							targetOffset = $hash.offset().top;
						}
					} else {
						targetOffset = 0;
					}
					$htmlBody.stop();
					var hash = this.hash;
					$htmlBody.animate({scrollTop: targetOffset}, 1000, 'easeInOutQuad', function() {
						location.hash = hash;
					});
				});
				
				$('.overlay').click(function(event) {
					$(this).find('img').detach();
					$(this).css('display', 'none');
				});
				
				$content.find('img').each(function() {
					$img = $(this);
					if ($img.width() == $content.width()) {
						$img.css({'cursor':'pointer'});
						$img.click(function(event) {
							$('.overlay').append('<img src="' + $(event.target).attr('src') + '" />');
							$('.overlay').css('display', 'block');
						});
					}
				});
			});