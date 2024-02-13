import React from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick.css";
import "slick-carousel/slick/slick-theme.css";
import PictureArea from "./PictureArea";
import styled from "styled-components";

const SilderContainer = styled.div`
  height: 530px;
`;
const StyledSlider = styled(Slider)`
  position: relative;
  .slick-prev::before,
  .slick-next::before {
    font-size: 40px;
    display: none;
  }
  .slick-list {
    border-radius: ${(props) => props.theme.radiuses.radius_m};
  }
  .slick-next {
    right: 25px;
    z-index: 1;
  }
  .slick-prev {
    left: 5px;
    z-index: 1;
  }
  .slick-dots {
    bottom: -50px;
    height: 40px;

    & > li {
      width: 40px;
      filter: brightness(0.5);

      &.slick-active {
        filter: brightness(1);
      }
    }
  }

  &:hover {
    .slick-prev::before,
    .slick-next::before {
      display: block;
    }
  }
`;

function Carousel({ imgList, thumbnail, size }) {
  const settings = {
    dots: true,
    infinite: imgList.length > 1,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: true,
    customPaging:
      thumbnail &&
      function (i) {
        return (
          <a>
            <img src={imgList[i]} style={{ width: "40px", height: "40px" }} />
          </a>
        );
      },
  };

  return (
    <SilderContainer>
      <StyledSlider {...settings} style={{ width: "480px" }}>
        {imgList.map((i) => {
          return <PictureArea img={i} size={size} key={i} />;
        })}
      </StyledSlider>
    </SilderContainer>
  );
}

export default Carousel;
