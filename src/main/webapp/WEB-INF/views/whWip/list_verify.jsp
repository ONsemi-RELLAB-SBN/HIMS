<%-- 
    Document   : list_verify
    Created on : Jun 21, 2023, 5:08:59 PM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
    </s:layout-component>
    <s:layout-component name="page_css_inline">
        <style>
            @media print {
                table thead {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
                table tbody {
                    border-top: #000 solid 2px;
                    border-bottom: #000 solid 2px;
                }
            }
            .dataTables_wrapper .dt-buttons {
                float:none;  
                text-align:right;
            }
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management - WIP Verification</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>WIP Information</h2>
                        <form id="update_hardwareinventory_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateReceiveToVerify/${wipData.requestId}" method="post">
                            <input type="hidden" name="refId" value="${wipData.requestId}" />
                            <div class="form-group">
                                <label for="gtsNo" class="col-lg-3 control-label">GTS No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="gtsNo" name="gtsNo" value="${wipData.gtsNo}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassNo" class="col-lg-3 control-label">RMS Event No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="rms" name="rms" value="${wipData.rmsEvent}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="materialPassExpiry" class="col-lg-3 control-label">Intervals</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="intervals" name="intervals" value="${wipData.intervals}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentType" class="col-lg-3 control-label">Quantity</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="quantity" name="quantity" value="${wipData.quantity}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="equipmentId" class="col-lg-3 control-label">Shipment Date</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="shipDate" name="shipDate" value="${wipData.shipmentDate}" readonly>
                                </div>
                            </div>
                            <div id = "alert_placeholder2"></div>
                            <a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>
<!--                            <div class="pull-right">
                                <button type="reset" class="btn btn-secondary cancel">Reset</button>
                                <button type="submit" class="btn btn-primary">Save</button>
                            </div>-->
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>
            </div>
                            
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Scan Trip Ticket</h2>
                        <form id="add_mp_list_form" class="form-horizontal" role="form" action="${contextPath}/whWip/updateReceive" method="post">
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">Scan Trip Ticket *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="tripTicket" name="tripTicket" placeholder="" value="" autofocus="autofocus">
                                </div>
                            </div>
                            <!--<a href="${contextPath}/whWip/listNew" class="btn btn-info pull-left"><i class="fa fa-reply"></i> Back</a>-->
                            <div class="pull-right">
                                <button type="submit" class="btn btn-primary">Verify</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>	
            </div>
        </div>
        <hr class="separator">
    </s:layout-component>
    <s:layout-component name="page_js">
        <script src="${contextPath}/resources/validation/jquery.validate.min.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
        <script src="${contextPath}/resources/private/js/bootstrap-datepicker.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/dataTables.buttons.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.print.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.flash.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                // CODE TO DISABLE THE FEATURE
//                $('#inventoryRack').bind('copy paste cut', function (e) {
//                    e.preventDefault();
//                });

                $(".cancel").click(function () {
                    validator.resetForm();
                });
                
                var tripT = $('#tripTicket');
                var rmsNo = $('#rms');
                var trip2 = document.update_hardwareinventory_form.tripTicket;
                
                var value = document.getElementById("tripTicket").value;
                
                $('#tripTicket').keyup(function () {
//                    oTable.search($(this).val()).draw();
                    console.log("log tigger the function again");
                    console.log("trip ticker :: "+tripT);
                    console.log("rms number  :: "+rmsNo);
                    console.log("TTTTTTTTTT  :: "+trip2);
                    // check if the data same with rms event
                });
                
            });
        </script>
    </s:layout-component>
</s:layout-render>