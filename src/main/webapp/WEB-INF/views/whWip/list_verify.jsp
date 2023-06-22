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
        </style>
    </s:layout-component>
    <s:layout-component name="page_container">
        <div class="col-lg-12">
            <h1>WIP Management - TO BE VERIFY</h1>
            <div class="row">
                <div class="col-lg-12">
                    <div class="main-box clearfix">
                        <div class="clearfix">
                            <h2 class="pull-left">PAGE WIP TO BE VERIFY</h2>
<!--                            <div class="filter-block pull-right">
                                <a href="${contextPath}/whWip/listReceive" class="btn btn-primary pull-right">
                                    <i class="fa fa-pencil-square-o fa-lg"></i> Scan GTS No
                                </a>
                            </div>-->
<!--                        <div class="filter-block pull-right">
                                <a href="${contextPath}/wh/whRequest/ship" data-toggle="modal" class="btn btn-primary pull-right">
                                    <i class="fa fa-bars fa-lg"></i> View Hardware Queue for Shipment
                                </a>
                            </div>-->
                            <div class="filter-block pull-right">
                            </div>
                        </div>
                        <hr/>
                        <div class="clearfix">
                            <div class="form-group pull-left">
                                <select id="dt_spml_rows" class="form-control">
                                    <option value="10">10</option>
                                    <option value="25">25</option>
                                    <option value="50">50</option>
                                    <option value="100">100</option>
                                </select>
                            </div>
                            <div class="filter-block pull-right">
                                <div id="dt_spml_tt" class="form-group pull-left" style="margin-right: 5px;">
                                </div>
                                <div class="form-group pull-left" style="margin-right: 0px;">
                                    <input id="dt_spml_search" type="text" class="form-control" placeholder="<f:message key="general.label.search"/>">
                                    <i class="fa fa-search search-icon"></i>
                                </div>
                            </div>
                        </div>
                        <div class="table-responsive">
                            <table id="dt_spml" class="table">
                                <thead>
                                    <tr>
                                        <th><span>No</span></th>
                                        <th><span>Request ID</span></th> 
                                        <th><span>GTS NO</span></th>
                                        <th><span>RMS Event</span></th>
                                        <th><span>Intervals</span></th>
                                        <th><span>Quantity</span></th>
                                        <th><span>Shipment Date</span></th>
                                        <th><span>Status</span></th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${wipList}" var="whWip" varStatus="whWipLoop">
                                        <tr>
                                            <td><c:out value="${whWipLoop.index+1}"/></td>
                                            <td><c:out value="${whWip.requestId}"/></td>
                                            <td><c:out value="${whWip.gtsNo}"/></td>
                                            <td><c:out value="${whWip.rmsEvent}"/></td>
                                            <td><c:out value="${whWip.intervals}"/></td>
                                            <td><c:out value="${whWip.quantity}"/></td>
                                            <td><c:out value="${whWip.shipmentDate}"/></td>
                                            <td><c:out value="${whWip.status}"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <hr class="separator">
    </s:layout-component>
    <s:layout-component name="page_js">
        <!--<script src="${contextPath}/resources/private/datatables/js/buttons.html5.min.js"></script>-->
    </s:layout-component>
    <s:layout-component name="page_js_inline">
        <script>
        </script>
    </s:layout-component>
</s:layout-render>