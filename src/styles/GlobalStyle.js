import { createGlobalStyle } from "styled-components";
import reset from "styled-reset";
import { MIN_WIDTH } from "../constants/layouts";

const GlobalStyle = createGlobalStyle`
    ${reset}
    
    html, body, #root{
        width: 100%;
        height:100%;
        font-family: Pretendard;
    }

    body{
        min-width: ${MIN_WIDTH};
    }

    #root{
        overflow-y: scroll;
        display: flex;
        flex-direction:column;
    }

    main{
        flex:1;
    }

    *{
        font-family: Pretendard;
    }
    
    button{
        background: inherit;
        border:none;
        box-shadow:none;
        border-radius:0;
        padding:0;
        overflow:visible;
        cursor:pointer
    }

    dialog {
        border: 0px;
    }

    a {
        text-decoration: none; 
    }

    .show{
        animation-name: show;
        -webkit-animation-name: show;

        animation-duration: 0.2s;
        -webkit-animation-duration: 0.2s;

        animation-timing-function: ease-out;
        -webkit-animation-timing-function: ease-out;
    }

    @keyframes show {
        0% {
        opacity: 0;
        }
        100% {
        opacity: 1;
        }
    }

    @-webkit-keyframes show {
        0% {
        opacity: 0;
        }
        100% {
        opacity: 1;
        }
    }
`;

export default GlobalStyle;
