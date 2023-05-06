import React from "react";
import Logo from "@theme/Logo";
export default function NavbarLogo() {
  return (
    <Logo
      className="navbar__brand flex"
      imageClassName="navbar__logo flex flex-grow-left flex-right"
      titleClassName="navbar__title text--truncate flex flex-grow-right flex-left"
    />
  );
}
