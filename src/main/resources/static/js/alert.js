
function dateNull(){
	$("#modal-header-content").text("Error");
	$("#modal-body-content").text("Date not null");
	$("#alert-modal").modal("show");
}
function dateInvalid(){
	$("#modal-header-content").text("Error");
	$("#modal-body-content").text("Start date cannot be after End date!");
	$("#alert-modal").modal("show");
}
function dataEventNull(){
	$("#modal-header-content").text("Error");
	$("#modal-body-content").text("Cant found events data!");
	$("#alert-modal").modal("show");
}
function dataCandidateNull(){
	$("#modal-header-content").text("Error");
	$("#modal-body-content").text("Cant found candidates data!");
	$("#alert-modal").modal("show");
}
function dataNull(){
	$("#modal-header-content").text("Error");
	$("#modal-body-content").text("Data Not Null!! Please select data!");
	$("#alert-modal").modal("show");
}
function testValue(){
	return "Hello";
}