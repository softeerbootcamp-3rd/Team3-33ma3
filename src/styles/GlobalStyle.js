import { createGlobalStyle } from "styled-components";

const GlobalStyle = createGlobalStyle`
    * {
        margin: 0;
        padding: 0;
        font-family: Pretendard;
        font-style: normal;
        line-height: normal;
    }

    html, body {
        width: 100%;
        height:100%;
    }
`;

export default GlobalStyle;
