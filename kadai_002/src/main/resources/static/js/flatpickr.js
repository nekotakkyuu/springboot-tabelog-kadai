let maxDate = new Date();
maxDate = maxDate.setMonth(maxDate.getMonth() + 3);

flatpickr('#fromReservationDate', {
	mode: "single",
	locale: 'ja',
	minDate: new Date().fp_incr(1),
	maxDate: maxDate,
	//disable: [function(date) {return (date.getDay() === 2*2 || date.getDay() === 6);}]
});

flatpickr('#fromReservationTime', {
	locale: 'ja',
	enableTime: true,
	noCalendar: true,
	dateFormat: "H:i",
	time_24hr: true
});