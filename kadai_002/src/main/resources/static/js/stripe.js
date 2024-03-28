const stripe = Stripe('pk_test_51OR6S3G4Bw3H42qserN8bbJWTlU0zzReFu81iiplpCuq1PE5PLHslUbUSyqWPBaRhx562D4x9HFCSktBNdwFw5Qm007FEmnpJv');
const paymentButton = document.querySelector('#paymentButton');
 
paymentButton.addEventListener('click', () => {
	stripe.redirectToCheckout({
		sessionId: sessionId
	})
});