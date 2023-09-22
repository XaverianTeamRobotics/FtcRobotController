// eslint-disable-next-line @typescript-eslint/no-var-requires
const defaultTheme = require("tailwindcss/defaultTheme");
// eslint-disable-next-line @typescript-eslint/no-var-requires
const plugin = require("tailwindcss/plugin");
/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      fontFamily: {
        "sans": [ "CozetteVector", ...defaultTheme.fontFamily.sans ],
      },
    }
  },
  plugins: [
    plugin(function({ addVariant }) {
      addVariant("hover-focus-active", [ "&:hover", "&:focus", "&:active" ]);
      addVariant("hover-focus", [ "&:hover", "&:focus" ]);
      addVariant("hover-active", [ "&:hover", "&:active" ]);
      addVariant("focus-active", [ "&:focus", "&:active" ]);
      addVariant("pointer-fine", "@media (pointer: fine)");
      addVariant("pointer-coarse", "@media (pointer: coarse)");
      addVariant("pointer-none", "@media (pointer: none)");
    }),
    require("@tailwindcss/forms"),
  ],
};
