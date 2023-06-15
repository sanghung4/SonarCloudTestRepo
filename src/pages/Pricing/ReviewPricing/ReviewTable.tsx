import React, { useState } from "react";
import { MdExpandLess, MdExpandMore, MdOutlineDeleteForever } from "react-icons/md";
import { usePricing } from "../PricingProvider";
import { StagedItem } from "../types";
import { formatToUSD } from "../utils";
import { ReviewTableProps } from "./types";

export const ReviewTable = ({ headers, data, type }: ReviewTableProps) => {
  // - HOOKS - //
  const { removeNewPrice } = usePricing();

  // - STATE - //
  const [expandedRow, setExpandedRow] = useState(-1);

  // - ACTIONS - //
  const toggleExpandedRow = (index: number) => {
    setExpandedRow((prev) => (prev === index ? -1 : index));
  };

  const handleRemovePrice = (item: StagedItem) => {
    removeNewPrice(type, item);
  };

  // - HELPERS - //
  const newPriceMargin = (newPrice: number, standardCost: number) => {
    const cost = standardCost || 100;
    const margin = Math.round(((newPrice - cost) / newPrice) * 1000) / 10;
    return newPrice > 0 ? `${margin}%` : `${0}%`;
  };

  // - JSX - //
  return (
    <table className='table-auto w-full rounded text-left overflow-x-scroll shadow-md'>
      <colgroup>
        <col className='w-[25%]' />
        <col className='w-[25%]' />
        <col className='w-[15%]' />
        <col className='w-[15%]' />
        <col />
        <col className='w-0' />
      </colgroup>
      <thead className='bg-primary-1-100 text-white font-bold text-xs uppercase'>
        <tr>
          {headers.map((col, index) => (
            <th className='px-2 py-3 first:pl-8 last:pr-8 whitespace-nowrap' key={index}>
              {col}
            </th>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((item, index) => (
          <React.Fragment key={index}>
            {/* Main Row */}
            <tr className='text-sm border-b last:border-b-0'>
              {/* Customer Cell */}
              <td className='relative pl-8 pr-2 py-6'>
                <p>{item.customerDisplayName}</p>
                <p className='text-secondary-2-100'>{item.customerId}</p>
                <button
                  className='absolute text-xl text-secondary-2-100'
                  onClick={() => toggleExpandedRow(index)}
                >
                  {expandedRow !== index ? <MdExpandMore /> : <MdExpandLess />}
                </button>
              </td>
              {/* Product Cell */}
              <td className='px-2 py-6'>
                <p>{item.displayName}</p>
                <p className='text-secondary-2-100'>{item.productId}</p>
              </td>
              {/* CMP / Current Special Cell */}
              <td className='px-2 py-6'>
                {type === "created"
                  ? formatToUSD(item.cmpPrice)
                  : formatToUSD(item.currentSpecial ?? 0)}
              </td>
              {/* Update Price Cell */}
              <td className='px-2 py-6'>
                <p>{formatToUSD(item.newPrice)}</p>
                <p className='text-secondary-2-100'>
                  GM: {newPriceMargin(item.newPrice, item.standardCost)}
                </p>
              </td>
              {/* Price Type / Delete Cell */}
              <td className='px-2 py-6'>{item.priceCategory}</td>
              {/* Delete Cell */}
              <td className='pl-2 pr-8 py-6 text-2xl'>
                <button className='block m-auto' onClick={() => handleRemovePrice(item)}>
                  <MdOutlineDeleteForever />
                </button>
              </td>
            </tr>
            {/* Expanded Row */}
            {expandedRow === index && (
              <tr className='border-b last:border-b-0 text-xs text-center'>
                <td className='px-8 py-3' colSpan={6}>
                  <div className='space-x-6 flex justify-evenly'>
                    <span>Write Name: {item.changeWriterDisplayName}</span>
                    <span>Branch: {item.branch}</span>
                    {type === "modified" && <span>Rate Card: {item.rateCardName}</span>}                   
                    <span>Territory: {item.territory || "-"}</span>
                    <span>Standard Cost: {formatToUSD(item.standardCost)}</span>
                    <span>Date Created: {"-"}</span>
                  </div>
                </td>
              </tr>
            )}
          </React.Fragment>
        ))}
      </tbody>
    </table>
  );
};
