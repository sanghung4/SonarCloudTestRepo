import React, { useState, useEffect } from 'react';
import classNames from 'classnames';

import './styles.scss';

/**
 * Types
 */
type SliderProps = {
  children: React.ReactNode[];
  testId?: string;
};

/**
 * Component
 */
function Slider(props: SliderProps) {
  const [slideIndex, setIndex] = useState(0);

  useEffect(() => {
    const id = setInterval(
      () => setIndex((state) => (state + 1) % props.children?.length),
      15000
    );

    return () => clearInterval(id);
  });

  const renderChildren = () => {
    return React.Children.map(props.children, (item, i) => {
      if (i !== slideIndex) {
        return;
      }

      return (
        <div className="slider-img" key={`slider-img${i}`}>
          {item}
        </div>
      );
    });
  };

  const renderControls = () =>
    React.Children.map(props.children, (_, i) => {
      const activeBtn = classNames('slider-button', {
        'slider-button--active': i === slideIndex
      });

      return (
        <button
          type="button"
          title="slide"
          className={activeBtn}
          onClick={() => setIndex(i)}
          key={`slider-button-${i}`}
        />
      );
    });

  return (
    <div className="slider" data-testid={props.testId}>
      {renderChildren()}
      <div className="slider-controls">{renderControls()}</div>
    </div>
  );
}

export default Slider;
