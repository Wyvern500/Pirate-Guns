package com.jg.jgpg.client.handlers;

import java.util.HashMap;
import java.util.Map;

import com.jg.jgpg.client.animations.Easing;
import com.jg.jgpg.utils.LogUtils;

public class EasingHandler {
	
	private Map<String, Easing> easings;
	
	public EasingHandler() {
		easings = new HashMap<>();
		
		register("empty", (x) -> x);
		register("easeInSine", (x) -> (float) (1 - Math.cos((x * Math.PI) / 2)));
		register("easeOutSine", (x) -> (float) (Math.sin((x * Math.PI) / 2)));
		register("easeInOutSine", (x) -> (float) (-(Math.cos(Math.PI * x) - 1) / 2));

		register("easeInQuad", (x) -> (float) (x * x));
		register("easeOutQuad", (x) -> (float) (1 - (1 - x) * (1 - x)));
		register("easeInOutQuad", (x) -> (float) (x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2));

		register("easeInCubic", (x) -> (float) (x * x * x));
		register("easeOutCubic", (x) -> (float) (1 - Math.pow(1 - x, 3)));
		register("easeInOutCubic", (x) -> (float) (x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2));

		register("easeInQuart", (x) -> (float) (x * x * x * x));
		register("easeOutQuart", (x) -> (float) (1 - Math.pow(1 - x, 4)));
		register("easeInOutQuart", (x) -> (float) (x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2));

		register("easeInQuint", (x) -> (float) (x * x * x * x * x));
		register("easeOutQuint", (x) -> (float) (1 - Math.pow(1 - x, 5)));
		register("easeInOutQuint",
				(x) -> (float) (x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2));

		register("easeInExpo", (x) -> (float) (x == 0 ? 0 : Math.pow(2, 10 * x - 10)));
		register("easeOutExpo", (x) -> (float) (x == 1 ? 1 : 1 - Math.pow(2, -10 * x)));
		register("easeInOutExpo", (x) -> (float) (x == 0 ? 0
				: x == 1 ? 1 : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2 : (2 - Math.pow(2, -20 * x + 10)) / 2));

		register("easeInCirc", (x) -> (float) (1 - Math.sqrt(1 - Math.pow(x, 2))));
		register("easeOutCirc", (x) -> (float) (Math.sqrt(1 - Math.pow(x - 1, 2))));
		register("easeInOutCirc", (x) -> (float) (x < 0.5 ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
				: (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2));

		register("easeInBack", (x) -> (float) (2.70158 * x * x * x - 1.70158f * x * x));
		register("easeOutBack", (x) -> (float) (1 + 2.70158 * Math.pow(x - 1, 3) + 1.70158 * Math.pow(x - 1, 2)));
		register("easeInOutBack",
				(x) -> (float) (x < 0.5 ? (Math.pow(2 * x, 2) * ((2.5949095f + 1) * 2 * x - 2.5949095f)) / 2
						: (Math.pow(2 * x - 2, 2) * ((2.5949095f + 1) * (x * 2 - 2) + 2.5949095f) + 2) / 2));

		register("easeInElastic", (x) -> (float) (x == 0 ? 0
				: x == 1 ? 1 : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * ((2 * Math.PI) / 3))));
		register("easeOutElastic", (x) -> (float) (x == 0 ? 0
				: x == 1 ? 1 : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75f) * ((2 * Math.PI) / 3)) + 1));
		register("easeInOutElastic", (x) -> (float) (x == 0 ? 0
				: x == 1 ? 1
						: x < 0.5
								? -(Math.pow(2, 20 * x - 10) * Math.sin((20 * x - 11.125) * ((2 * Math.PI) / 4.5))) / 2
								: (Math.pow(2, -20 * x + 10) * Math.sin((20 * x - 11.125) * ((2 * Math.PI) / 4.5))) / 2
										+ 1));

		register("easeInBounce", (x) -> (float) (1 - getEasing("easeOutBounce").get(1 - x)));
		register("easeOutBounce", (x) -> {
			float n1 = 7.5625f;
			float d1 = 2.75f;

			if (x < 1 / d1) {
				return n1 * x * x;
			} else if (x < 2 / d1) {
				return (float) (n1 * (x -= 1.5 / d1) * x + 0.75);
			} else if (x < 2.5 / d1) {
				return (float) (n1 * (x -= 2.25 / d1) * x + 0.9375);
			} else {
				return (float) (n1 * (x -= 2.625 / d1) * x + 0.984375);
			}
		});
		register("easeInOutBounce", (x) -> (float) (x < 0.5 ? (1 - getEasing("easeOutBounce").get(1 - 2 * x)) / 2
				: (1 + getEasing("easeOutBounce").get(2 * x - 1)) / 2));
		LogUtils.log("EasingHandler", "Registering easings");
	}
	
	public void register(String name, Easing e) {
		easings.put(name, e);
	}
	
	public boolean has(String easing) {
		return easings.containsKey(easing);
	}
	
	public Easing getEasing(String name) {
		return easings.get(name);
	}
	
}
