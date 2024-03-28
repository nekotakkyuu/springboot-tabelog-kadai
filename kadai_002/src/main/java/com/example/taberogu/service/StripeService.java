package com.example.taberogu.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.taberogu.entity.User;
import com.example.taberogu.form.SubscriptionForm;
import com.example.taberogu.repository.UserRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {
	@Value("${stripe.api-key}")
	private String stripeApiKey;
	
	private String priceId = "price_1OrdNNG4Bw3H42qs0C2leKLA";
	
	private final UserService userService;
	private final UserRepository userRepository;
	
	public StripeService(UserService userService, UserRepository userRepository) {
		this.userService = userService;
		this.userRepository = userRepository;
	}
	
	// セッションを作成し、Stripeに必要な情報を返す
	public String subscriptionStripeSession(User user, SubscriptionForm subscriptionForm ,HttpServletRequest httpServletRequest) {
		Stripe.apiKey = stripeApiKey;
		String requestUrl = new String(httpServletRequest.getRequestURL());
		
		//出力確認
		//System.out.println(user.getId().toString());
		//System.out.println(requestUrl);
		
		SessionCreateParams params =
			SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
					SessionCreateParams.LineItem.builder()
						.setQuantity(1L)
						.setPrice(priceId)
						.build())
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				.setSuccessUrl(requestUrl.replaceAll("/subscription", "/login") + "?reserved")
				.setCancelUrl(requestUrl)
				.setSubscriptionData(
					SessionCreateParams.SubscriptionData.builder()
						.putMetadata("user_id", subscriptionForm.getUser_id().toString())
						.putMetadata("role_id", subscriptionForm.getRole_id().toString())
						.build())
			.build();
		
		userService.subscription(subscriptionForm.getUser_id());
		try {
			Session session = Session.create(params);
			System.out.println(session.getSubscription());
			return session.getId();
		} catch (StripeException e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/*
	// セッションから有料会員登録を取得し、UserServiceクラスを介してデータベースの会員情報(Role)を更新する  
	public void processSessionCompleted(Event event) {
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
		
		optionalStripeObject.ifPresent(stripeObject -> {
			Session session = (Session)stripeObject;
			SessionRetrieveParams params = SessionRetrieveParams.builder().addExpand("subscription").build();

			try {
				session = Session.retrieve(session.getId(), params, null);
				Map<String, String> subscriptionObject = session.getSubscriptionObject().getMetadata();
				
				//出力確認
				//System.out.println("StripeServiceの89行目です。");
				//System.out.println(session.getSubscription());
				
	//			userService.subscription(subscriptionObject);
			} catch (StripeException e) {
				e.printStackTrace();
			}
		});
	}
	

	//サブスクリプションをキャンセルする。
	public void cancelSubscription(SubscriptionForm user) {
		Stripe.apiKey = stripeApiKey;
		
		String subscriptionId = userRepository.findByRoleId(user.getRole_id()).getName();
		
		SubscriptionCancelParams params = SubscriptionCancelParams.builder().build();
		try {
			Subscription resource = Subscription.retrieve(subscriptionId);
			resource.cancel(params);
			userService.cancel(user);
			} catch (StripeException e) {
			e.printStackTrace();
		}
	}
	*/
	
//	public void processSubscriptionDeleted(Event event) {
//		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();
//		
//		optionalStripeObject.ifPresent(stripeObject -> {
//			Subscription subscription = (Subscription)stripeObject;
//
//			try {
//				subscription = Subscription.retrieve(subscription.getId());
//				Map<String, String> subscriptionObject = subscription.getMetadata();
//				
//				//出力確認
//				System.out.println("StripeServiceの133行目です。");
//				System.out.println(subscriptionObject);
//				
//				subscriptionService.delete(subscriptionObject);
//				userService.roleUpdate(subscriptionObject);
//			} catch (StripeException e) {
//				e.printStackTrace();
//			}
//		});
//	}

}