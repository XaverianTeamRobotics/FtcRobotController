export const sidebarEventManager = (): EventListener => {
  const listener = evt => {
    window.requestAnimationFrame(() => {
      const elem = document.getElementsByClassName("theme-doc-sidebar-menu menu__list")[0];
      const container = document.querySelector("nav[aria-label='Docs sidebar'].menu.thin-scrollbar");
      if(elem === null || elem === undefined || container === null || container === undefined)return;
      if((evt as CustomEvent).detail.overflow) {
        elem.classList.add("theme-doc-sidebar-item-category-level-1-margin-right");
        container.classList.add("menu-margin-right");
      }else{
        elem.classList.remove("theme-doc-sidebar-item-category-level-1-margin-right");
        container.classList.remove("menu-margin-right");
      }
    });
  };
  document.addEventListener("sidebaroverflowed", listener);
  return listener;
};