import React, { Fragment } from "react";
export default function FooterCopyright({ copyright }) {
  return (
    <Fragment>
      <div className="footer__copyright">
        <div
          className={"footer__copyright__c"}
          style={{ whiteSpace: "nowrap" }}
          // Developer provided the HTML, so assume it's safe.
          // eslint-disable-next-line react/no-danger
          dangerouslySetInnerHTML={{ __html: copyright }}
        />
        <div className={"footer__copyright__spacer"}/>
        <div className={"footer__copyright__attr"} style={{ whiteSpace: "nowrap" }}>
          Built by <wbr/><a href={"https://www.thomasricci.dev"}>Thomas Ricci</a> <wbr/>and <a href={"https://api.github.com/repos/XaverianTeamRobotics/FtcRobotController/contributors"}>these great people</a>.
        </div>
      </div>
    </Fragment>
  );
}
