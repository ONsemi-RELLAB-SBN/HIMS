<%-- 
    Document   : list_to_ship
    Created on : Aug 25, 2023, 10:50:40 AM
    Author     : zbqb9x
--%>

<%@page contentType="text/html;charset=UTF-8" %>
<%@include file="/WEB-INF/base/taglibs.jsp" %>
<s:layout-render name="/WEB-INF/base/base.jsp">
    <s:layout-component name="page_css">
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/jquery.dataTables.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/private/datatables/css/buttons.dataTables.min.css" type="text/css" />
        <link rel="stylesheet" href="${contextPath}/resources/css/005.css" type="text/css" />
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
            <h1>WIP Management [0 hours]</h1>
            <div class="row">
                <div class="col-lg-8">
                    <div class="main-box">
                        <h2>Scan Box Number</h2>
                        <form id="check_rack_shelf" class="form-horizontal" role="form" action="${contextPath}/wip0hour/updateToShip" method="post">
                            <div class="form-group">
                                <label for="checkboxno" class="col-lg-3 control-label">Box No.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="checkboxno" name="checkboxno" value="${wipData.rmsEvent}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="checkrack" class="col-lg-3 control-label">Rack.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="checkrack" name="checkrack" value="${wipData.rack}" readonly>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="checkshelf" class="col-lg-3 control-label">Shelf.</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="checkshelf" name="checkshelf" value="${wipData.shelf}" readonly>
                                </div>
                            </div>
                                
                            <div class="form-group">
                                <label for="boxNo" class="col-lg-3 control-label">Box Number *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="boxNo" name="boxNo" value="" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="rack" class="col-lg-3 control-label">Rack *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="rack" name="rack" value="" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="shelf" class="col-lg-3 control-label">Shelf *</label>
                                <div class="col-lg-8">
                                    <input type="text" class="form-control" id="shelf" name="shelf" value="" required>
                                </div>
                            </div>
                            <a href="${contextPath}/wip0hour/request" class="btn btn-info pull-left" style="font-family:'Orbitron', monospace;"><i class='bx bxs-chevron-left bx-fw'></i> Back</a>
                            <div class="pull-right">
                                <button type="submit" class="btn btn-primary" style="font-family:'Orbitron', monospace;"><i class='bx bxs-shield-plus bx-fw' style='color:#ffffff'  ></i> Register</button>
                            </div>
                            <div class="clearfix"></div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </s:layout-component>
    <s:layout-component name="page_js">
        <!--print-->
        <script src="${contextPath}/resources/private/datatables/js/jquery.dataTables.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/dataTables.buttons.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.print.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.flash.min.js"></script>
        <script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>
        <script src="${contextPath}/resources/validation/jquery.validate.js"></script>
        <script src="${contextPath}/resources/validation/additional-methods.js"></script>
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
            $(document).ready(function () {
                jQuery.extend(jQuery.validator.messages, {
                    required: "This field is required by system.",
                    equalTo: "The value not match! Please re-scan.",
//                    equalToA: "Box number is not match! Please re-scan.",
//                    equalToR: "Rack value is not match! Please re-scan.",
//                    equalToS: "Shelf value is not match! Please re-scan.",
                    email: "Please enter a valid email."
                });
                
//                oTable.search($(this).val()).draw();
                
//                oTable = $('#dt_spml').DataTable({
////                    dom: 'Brtip'
//                });
                
//                $('#dt_spml_search').keyup(function () {
//                    oTable.search($(this).val()).draw();
//                });
                
                $('#boxNo').keyup(function () {
                    oTable.search($(this).val()).draw();
                });
                
//                $("#dt_spml_rows").change(function () {
//                    oTable.page.len($(this).val()).draw();
//                });
//                
//                $(".cancel").click(function () {
//                    validator.resetForm();
//                });
                
                var validator = $("#check_rack_shelf").validate({
                    rules: {
                        boxNo: {
                            required: true,
                            equalTo: "#checkboxno"
                        },
                        rack: {
                            required: true,
                            equalTo: "#checkrack"
                        },
                        shelf: {
                            required: true,
                            equalTo: "#checkshelf"
                        }
                    }
                });
                
                $('#boxNo').bind('copy paste cut', function (e) {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                $('#rack').bind('copy paste cut', function (e) {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste  
                });
                
                $('#shelf').bind('copy paste cut', function (e) {
                    e.preventDefault(); //this line will help us to disable cut,copy,paste 
                });
            });
            
        </script>
    </s:layout-component>
</s:layout-render>